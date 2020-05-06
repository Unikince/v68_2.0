package com.dmg.bjlserver.business.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bjlserver.business.config.dic.BjlTableDic;
import com.dmg.bjlserver.business.config.wrapper.BjlTableWrapper;
import com.dmg.bjlserver.business.model.Cache;
import com.dmg.bjlserver.business.model.Constant;
import com.dmg.bjlserver.business.model.game.Card;
import com.dmg.bjlserver.business.model.game.GameStage;
import com.dmg.bjlserver.business.model.game.SeatPlayer;
import com.dmg.bjlserver.business.model.game.SeatRecord;
import com.dmg.bjlserver.business.model.room.Seat;
import com.dmg.bjlserver.business.model.room.Table;
import com.dmg.bjlserver.business.platform.model.Player;
import com.dmg.bjlserver.business.platform.service.PlayerService;
import com.dmg.bjlserver.business.robot.RobotMgr;
import com.dmg.bjlserver.business.service.CardUtil;
import com.dmg.bjlserver.business.service.GameHistory;
import com.dmg.bjlserver.core.work.WorkerGroup;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.pb.java.Bjl;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.model.dto.GameRecordDTO.GameRecordDTOBuilder;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.server.common.util.RoundIdUtils;
import com.googlecode.protobuf.format.JsonFormat;

import lombok.extern.log4j.Log4j2;

/**
 * 百家乐逻辑处理
 */
@Log4j2
@Component
public class BjlMgr {
    @Value("${game-id}")
    private int gameId;
    @Autowired
    private WorkerGroup workerGroup;
    @Autowired
    private BjlMsgMgr msgMgr;
    @Autowired
    private BjlTableDic roomDic;
    @Autowired
    public GameHistory gameHistory;
    @Autowired
    public PlayerService playerService;
    @Autowired
    private MQProducer defautProducer;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private GameOnlineChangeUtils gameOnlineChangeUtils;

    /** 停服标注 */
    private boolean STOP_SERVICE = false;

    /** 百家乐管理类单例 */
    private static BjlMgr INSTANCE = null;

    /** 获取百家乐管理类单例 */
    public static BjlMgr getInstance() {
        return INSTANCE;
    }

    /** 初始化方法 */
    @PostConstruct
    private void init() {
        BjlMgr.INSTANCE = this;
        this.workerGroup.schedule(() -> {
            this.initRooms();
            this.startGame();
            this.workerGroup.schedule(() -> RobotMgr.getInstance().run(), 100, TimeUnit.SECONDS);
            this.msgMgr.roomInfoCache();
        }, 0, TimeUnit.MILLISECONDS);
    }

    /** 获取某个房间的配置数据 */
    public BjlTableWrapper getRoomConfig(int roomId) {
        BjlTableWrapper config = this.roomDic.get(roomId);
        if (config == null) {
            BjlMgr.log.error("[百家乐]get room config error!! roomId={}", roomId);
        }
        return config;
    }

    /**
     * 初始化房间
     */
    private void initRooms() {
        for (BjlTableWrapper config : this.roomDic.values()) {
            int tableId = config.getId();
            this.redisUtil.set(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + tableId, "0");
            this.redisUtil.set(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + tableId, "0");
            this.gameOnlineChangeUtils.initOnlineNum0(this.gameId, tableId);

            // 初始化房间
            Table table = new Table(config.getId(), config.getName(), Cache.getAreaMultiples(), config.getPlayerNum());
            // 初始化机器人
            RobotMgr.getInstance().addRoom(config.getId());
            // 初始化房间库存
            Cache.addTable(table);

            log.info("[百家乐] 初始化房间[{}][{}]", config.getId(), config.getName());
        }
    }

    /**
     * 开始游戏
     */
    private void startGame() {
        for (Table table : Cache.getAllTable()) {
            this.tableRestStage(table);
        }
    }

    /**
     * 场次休息阶段,保存战绩、刷新座位、刷新排名玩家、刷新无座玩家
     */
    private void tableRestStage(Table table) {
        // 战绩(牌局明细)
        this.saveTableRecord(table);
        // 保存战绩
        this.saveRecord(table);

        table.stage = GameStage.REST;
        table.round++;
        table.reset();

        // 刷新座位
        this.refreshSeats(table);
        // 刷新排名玩家
        this.refreshPlayerSeats(table);

        // msgMgr.sendBankerInfoMsg(table);
        this.msgMgr.sendRankPlayersMsg(table);
        this.msgMgr.sendOtherPlayerNumMsg(table);
        this.msgMgr.sendStageMsg(table, table.stage, Constant.REST_TIME);
        table.stageFuture = this.workerGroup.schedule(() -> this.tableBetStage(table), Constant.REST_TIME, TimeUnit.SECONDS);
        log.info("房间[{}]场次[{}]轮[{}]进入[{}]阶段", table.name, table.id, table.round, table.stage.desc);
    }

    /**
     * 场次下注阶段
     *
     * @param table
     */
    private void tableBetStage(Table table) {
        table.stage = GameStage.BET;
        table.startTime = new Date();

        this.msgMgr.sendStageMsg(table, table.stage, Constant.BET_TIME);
        // 每隔一秒通知一次金币变化
        table.noticeGoldFuture = this.workerGroup.scheduleWithFixedDelay(() -> this.msgMgr.sendOtherPlayersBetMsg(table), 1, 1, TimeUnit.SECONDS);
        table.stageFuture = this.workerGroup.schedule(() -> this.tableShuffleStage(table), Constant.BET_TIME, TimeUnit.SECONDS);
        log.info("房间[{}]场次[{}]轮[{}]进入[{}]阶段", table.name, table.id, table.round, table.stage.desc);
    }

    /**
     * 刷新座位，重置座位数据，离线的玩家清空座位
     *
     * @param table
     */
    private void refreshSeats(Table table) {
        int watchKickRound = this.roomDic.get(table.id).getWatchKickRound();
        for (Seat seat : table.seats) {
            long playerId = seat.playerId;
            if (playerId > 0) { // 不是庄家的情况下才会被清理
                // 通知玩家金币变化
                this.msgMgr.sendGoldChangeMsg(seat);
                boolean isTick = this.STOP_SERVICE || seat.robotExit || (seat.notBetRound >= watchKickRound) || (seat.offlineRound >= 1) && (table.banker != seat);
                if (isTick) {
                    if (!seat.robot) {
                        this.playerService.syncRoom(0, 0, seat.playerId);
                        this.redisUtil.decr(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + table.id, 1.0);
                        GameOnlineChangeUtils.decOnlineNum(this.gameId, table.id);
                    } else {
                        this.redisUtil.decr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + table.id, 1.0);
                    }
                    if (this.STOP_SERVICE) {
                        this.msgMgr.sendExitRoomMsg(seat, Bjl.BjlCode.STOP_SERVER);
                    } else {
                        this.msgMgr.sendExitRoomMsg(seat, Bjl.BjlCode.TIMEOUT_EXIT);
                    }
                }
                if (isTick) {
                    table.clearRankSeat(seat);
                    Cache.removeSeat(playerId);
                    log.info("玩家[{}][{}]未下注轮数[{}]离线轮数[{}]不符合要求[{}]被清理除房间[{}]", seat.playerId, seat.nickName, seat.notBetRound, seat.offlineRound, 1, table.name);
                    seat.clear();
                }
            }
            seat.reset();
        }
        table.sysSeat.reset();
    }

    /**
     * 刷新玩家排名,百人场自动落座设置为true且金币排名前6的玩家为有座玩家，其余为无座玩家
     *
     * @param table
     */
    private void refreshPlayerSeats(Table table) {
        Collections.sort(table.seats, (lhs, rhs) -> {
            if ((lhs.playerId > 0) && (rhs.playerId > 0)) {
                if (lhs.gold < rhs.gold) {
                    return 1;
                } else if (lhs.gold > rhs.gold) {
                    return -1;
                }
                return 0;
            } else if (lhs.playerId > 0) {
                return -1;
            } else if (rhs.playerId > 0) {
                return 1;
            }

            return lhs.order - rhs.order;
        });

        table.rankSeats.clear();
        for (int i = 0; i < table.seats.size(); i++) {
            Seat seat = table.seats.get(i);
            if ((seat.playerId > 0) && (seat != table.banker)) {
                table.rankSeats.add(table.seats.get(i));
            }
            if (table.rankSeats.size() >= Constant.RANK_SEAT_COUNT) {
                break;
            }
        }

        table.otherSeats.clear();
        for (int i = 0; i < table.seats.size(); i++) {
            Seat seat = table.seats.get(i);
            if ((seat.playerId > 0) && !table.rankSeats.contains(seat) && !table.banker.equals(seat)) {
                log.info("无座列表添加了玩家[{}]", seat.playerId);
                table.otherSeats.add(table.seats.get(i));
            }
        }
    }

    /**
     * 场次洗牌阶段
     *
     * @param table
     */
    private void tableShuffleStage(Table table) {
        // 洗牌时间(s)
        table.stage = GameStage.SHUFFLE;
        table.noticeGoldFuture.cancel(false);

        // 判断要不要洗牌
        if (table.needShuffle()) {
//        if (true) {
            // 清空线路图
            this.clearRoutes(table);
            final int cut = table.shuffle();
            // 发送洗牌信息
            this.msgMgr.sendShuffleMsg(table, Constant.SHUFFLE_TIME, cut);
            // 发送阶段消息
            this.msgMgr.sendStageMsg(table, table.stage, Constant.SHUFFLE_TIME);
            table.stageFuture = this.workerGroup.schedule(() -> this.tableGameStage(table), Constant.SHUFFLE_TIME, TimeUnit.SECONDS);
        } else {
            this.tableGameStage(table);
        }

    }

    /**
     * 场次游戏阶段
     *
     * @param table
     */
    private void tableGameStage(Table table) {
        table.stage = GameStage.GAMING;
        // 发牌
        this.dealCards(table);

        // 添加路线图
        Bjl.RouteInfo route = this.addRoute(table);

        // 结算下注玩家
        this.balanceSeat(table);

        // 场次下注的筹码是玩家定时获取的，游戏开始时再同步一次，保证每个玩家看到的筹码是一致的
        // 向场次中的所有玩家发送场次区域下注消息
        this.msgMgr.sendOtherPlayersBetMsg(table);
        // 发送游戏结算消息
        this.msgMgr.sendBalanceMsg(table, route);
        // 发送阶段消息
        this.msgMgr.sendStageMsg(table, table.stage, Constant.GAME_TIME);

        table.stageFuture = this.workerGroup.schedule(() -> {
            this.tableRestStage(table);
        }, Constant.GAME_TIME, TimeUnit.SECONDS);

        log.info("房间[{}][{}]场次[{}]轮[{}]进入[{}]阶段", table.name, table.id, table.id, table.round, table.stage.desc);
    }

    /**
     * 存取战绩（因 提前存取或造成客户端在下注后就查询出当前战绩 所以提到 休息前进行存储）
     */
    private void saveRecord(Table table) {
        long now = System.currentTimeMillis();
        // 保存我的牌局明细
        // this.roomDataService.updateAttr(BjlMgr.MODULE_ID, table.id, "" +
        // table.id, JSON.toJSONString(table.brnnTableRecord));
        long time1 = System.currentTimeMillis() - now;
        now = System.currentTimeMillis();

        for (Seat seat : table.seats) {
            if (seat.playerId <= 0) {
                continue;
            }
            // 发布跑马灯事件
            if ((seat.totalWinGold() - seat.totalBet()) > 100000) {
                // this.coordinateClient.publishEvent(new NoticeEvent(MODULE_ID,
                // seat.table.id, seat.nickName, seat.totalWinGold()));
            }

            long totalBet = seat.totalBet();
            long deltaGold = seat.totalWinGold() - totalBet;
            if ((deltaGold == 0) && (totalBet == 0)) {
                continue;
            }

            if (!seat.robot) {
                if (seat.seatRecord != null) {
                    this.gameHistory.addGameHistory(seat.playerId, seat.seatRecord);
                    seat.seatRecord = null;
                }
            }

            this.gameHistory.updatePlayerDayGold(seat.table.id, seat.table.id, seat.playerId, deltaGold);
            seat.table.profitListChanged = true;
        }

        log.info("saveRecord roomDataService耗时[{}]毫秒, redis耗时[{}]毫秒", time1, System.currentTimeMillis() - now);
    }

    /**
     * 发牌
     *
     * @param table
     */
    private void dealCards(Table table) {
        table.bankerCards.clear();
        table.playerCards.clear();

        table.bankerCards.add(table.cards.poll());
        table.playerCards.add(table.cards.poll());

        table.bankerCards.add(table.cards.poll());
        table.playerCards.add(table.cards.poll());

        Card b1, b2, b3 = null;
        Card p1, p2, p3 = null;

        b1 = table.bankerCards.get(0);
        b2 = table.bankerCards.get(1);

        p1 = table.playerCards.get(0);
        p2 = table.playerCards.get(1);

        // 庄家点数超过7点
        boolean bankerDone = CardUtil.isDone(b1, b2);
        // 闲家点数超过7点
        boolean playerDone = CardUtil.isDone(p1, p2);

        // 庄、闲是否需要补牌
        boolean bankerAdd, playerAdd;
        boolean bankerCanAdd;

        if (!bankerDone && !playerDone) {
            // 庄、闲均不超过7点，判断闲家是否需要补牌
            playerAdd = CardUtil.needAddCard(p1, p2);
            if (playerAdd) {
                p3 = table.cards.poll();
                table.playerCards.add(p3);
            }
            bankerCanAdd = CardUtil.canBankerAddCard(b1, b2, p3);
            if (bankerCanAdd) {
                bankerAdd = CardUtil.needAddCard(b1, b2);
                if (bankerAdd) {
                    b3 = table.cards.poll();
                    table.bankerCards.add(b3);
                }
            }
        }

        // 计算玩家输赢
        this.calculateResult(table);
        this.saveTableRecodes(table);
    }

    /**
     * 计算玩家输赢
     *
     * @param table
     */
    private void calculateResult(Table table) {
        Card b1, b2, b3 = null;
        Card p1, p2, p3 = null;
        // 是否出和
        boolean isDraw = false;

        b1 = table.bankerCards.get(0);
        b2 = table.bankerCards.get(1);
        if (table.bankerCards.size() > 2) {
            b3 = table.bankerCards.get(2);
        }

        p1 = table.playerCards.get(0);
        p2 = table.playerCards.get(1);
        if (table.playerCards.size() > 2) {
            p3 = table.playerCards.get(2);
        }

        // 庄家点数超过7点
        boolean bankerDone = CardUtil.isDone(b1, b2);
        // 闲家点数超过7点
        boolean playerDone = CardUtil.isDone(p1, p2);

        HashSet<Bjl.BetZoneType> winZone = new HashSet<>();
        HashSet<Bjl.BetZoneType> loseZone = new HashSet<>();

        // * 发牌结束
        if (bankerDone && playerDone) {
            // 庄家和闲家点数同时超过7点
            int bankerTotal = CardUtil.total(b1, b2, b3);
            int playerTotal = CardUtil.total(p1, p2, p3);

            if (bankerTotal == playerTotal) {
                winZone.add(Bjl.BetZoneType.DRAW);
                table.winner = Bjl.BetZoneType.DRAW;
            } else if (bankerTotal > playerTotal) {
                winZone.add(Bjl.BetZoneType.BANKBER);
                loseZone.add(Bjl.BetZoneType.PLAYER);
                loseZone.add(Bjl.BetZoneType.DRAW);
                table.winner = Bjl.BetZoneType.BANKBER;
            } else {
                winZone.add(Bjl.BetZoneType.PLAYER);
                loseZone.add(Bjl.BetZoneType.BANKBER);
                loseZone.add(Bjl.BetZoneType.DRAW);
                table.winner = Bjl.BetZoneType.PLAYER;
            }
        } else if (bankerDone) {
            // 仅庄家点数超过7点
            winZone.add(Bjl.BetZoneType.BANKBER);
            loseZone.add(Bjl.BetZoneType.PLAYER);
            loseZone.add(Bjl.BetZoneType.DRAW);
            table.winner = Bjl.BetZoneType.BANKBER;
        } else if (playerDone) {
            // 仅闲家点数超过7点
            winZone.add(Bjl.BetZoneType.PLAYER);
            loseZone.add(Bjl.BetZoneType.BANKBER);
            loseZone.add(Bjl.BetZoneType.DRAW);
            table.winner = Bjl.BetZoneType.PLAYER;
        } else {
            int bankerTotal = CardUtil.total(b1, b2, b3);
            int playerTotal = CardUtil.total(p1, p2, p3);

            if (bankerTotal == playerTotal) {
                winZone.add(Bjl.BetZoneType.DRAW);
                table.winner = Bjl.BetZoneType.DRAW;
            } else if (bankerTotal > playerTotal) {
                winZone.add(Bjl.BetZoneType.BANKBER);
                loseZone.add(Bjl.BetZoneType.PLAYER);
                loseZone.add(Bjl.BetZoneType.DRAW);
                table.winner = Bjl.BetZoneType.BANKBER;
            } else {
                winZone.add(Bjl.BetZoneType.PLAYER);
                loseZone.add(Bjl.BetZoneType.BANKBER);
                loseZone.add(Bjl.BetZoneType.DRAW);
                table.winner = Bjl.BetZoneType.PLAYER;
            }
        }

        // 庄对、闲对确定
        if (CardUtil.isPair(b1, b2)) {
            winZone.add(Bjl.BetZoneType.STREAK_BANKER);
        } else {
            loseZone.add(Bjl.BetZoneType.STREAK_BANKER);
        }

        if (CardUtil.isPair(p1, p2)) {
            winZone.add(Bjl.BetZoneType.STREAK_PLAYER);
        } else {
            loseZone.add(Bjl.BetZoneType.STREAK_PLAYER);
        }

        log.info("[百家乐]房间[{}]桌[{}]结果为： 庄:[{}] 闲:[{}] 输赢:[{}]", table.id, table.id, table.bankerCards, table.playerCards, winZone);

        // 重置
        table.banker.winGolds = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
        table.banker.tax = 0;

        BjlTableWrapper wrapper = this.roomDic.get(table.id);
        List<Integer> multi = Cache.getAreaMultiples();
        isDraw = winZone.contains(Bjl.BetZoneType.DRAW);
        for (Seat seat : table.seats) {

            if (seat.playerId <= 0) {
                continue;
            }

            // 重置
            seat.winGolds = new long[Bjl.BetZoneType.ZONE_MAX.getNumber()];
            seat.tax = 0;

            // 庄家、闲家点数相同（和局）
            if (isDraw) {
                {
                    // 庄区投注
                    long bankerAreaBet = seat.areaBets[Bjl.BetZoneType.BANKBER_VALUE];
                    // 闲区投注
                    long playerAreaBet = seat.areaBets[Bjl.BetZoneType.PLAYER_VALUE];
                    // 此处功能修改，从返还1/2下注金额修改为返还全部下注金额
                    seat.winGolds[Bjl.BetZoneType.BANKBER_VALUE] = bankerAreaBet;
                    seat.winGolds[Bjl.BetZoneType.PLAYER_VALUE] = playerAreaBet;
                }

                {
                    // 和区投注
                    long drawAreaBet = seat.areaBets[Bjl.BetZoneType.DRAW_VALUE];

                    long win = drawAreaBet * multi.get(Bjl.BetZoneType.DRAW_VALUE);
                    long winGold = drawAreaBet + win;
                    // 此处应该抽取净利润
                    long tax = (win * wrapper.getPumpRate()) / 10000;

                    seat.tax += tax;
                    seat.winGolds[Bjl.BetZoneType.DRAW_VALUE] = winGold - tax;
                    winZone.remove(Bjl.BetZoneType.BANKBER);
                    winZone.remove(Bjl.BetZoneType.PLAYER);
                    // 因为这里已经计算了和区域的输赢，所以要把它从赢的区域集合汇总移除
                    winZone.remove(Bjl.BetZoneType.DRAW);

                    table.banker.winGolds[Bjl.BetZoneType.DRAW_VALUE] -= win;
                }
            }

            for (int i = 0; i < Bjl.BetZoneType.ZONE_MAX_VALUE; i++) {
                Bjl.BetZoneType bt = Bjl.BetZoneType.forNumber(i);
                long areaBet = seat.areaBets[bt.getNumber()];
                if (winZone.contains(bt)) {
                    if (areaBet > 0) {
                        long winGold = areaBet * multi.get(bt.getNumber());
                        long tax = ((winGold) * wrapper.getPumpRate()) / 10000;
                        seat.winGolds[bt.getNumber()] = (winGold + areaBet) - tax;
                        table.banker.winGolds[bt.getNumber()] -= winGold;
                        seat.tax += tax;
                    }
                }

                if (loseZone.contains(bt)) {
                    if (areaBet > 0) {
//                        long win = seat.areaBets[bt.getNumber()];
                        seat.winGolds[bt.getNumber()] = 0;
                        // 庄家赢，抽取下注金额
                        long tax = ((areaBet) * wrapper.getPumpRate()) / 10000;
                        long total = areaBet - tax;
                        table.banker.tax += tax;
                        table.banker.winGolds[bt.getNumber()] += total;
                    }
                }
            }
        }
        log.info("[百家乐] 房间:[{}], 桌:[{}] 结束计算筹码赔率", table.id, table.id);
    }

    private void saveTableRecodes(Table table) {
        for (Seat seat : table.seats) {
            if ((seat.playerId > 0) && (seat.totalBet() > 0)) {
                this.saveSeatRecord(seat);
            }
        }
    }

    /**
     * 玩家进入房间,自动分桌
     *
     * @param playerId
     * @param roomId
     */
    public void enterRoom(Object playerId, int roomId) {
        if (this.STOP_SERVICE) {
            return;
        }
        Player player;
        if (playerId instanceof Player) {
            player = (Player) playerId;
        } else {
            player = this.playerService.getPlayer((Long) playerId);
            if (player == null) {
                player = this.playerService.getPlayerPlatform((Long) playerId);
            }
        }

        Seat seat = Cache.getSeat(player.getId());
        if (seat != null) {
            if (seat.playerId == 0) {
                Cache.removeSeat(player.getId());
            } else {
                seat.offlineRound = 0;
                seat.notBetRound = 0;
                this.msgMgr.sendEnterRoomMsg(seat);
                log.info("玩家[{}][{}]重新进入房间", seat.nickName, seat.playerId);
                return;
            }

        }

        Table table = Cache.getTable(roomId);
        if (table == null) {
            log.warn("玩家[{}][{}]进入的房间[{}]不存在", player.getNickName(), player.getId(), roomId);
            return;
        }

        if (!this.roomDic.get(table.id).isOpen()) {
            this.msgMgr.sendEnterRoomMsg(player.getId(), Bjl.BjlCode.ROOM_CLOSED);
            log.warn("玩家[{}][{}]进入的房间[{}]已经关闭", player.getNickName(), player.getId(), roomId);
            return;
        }

        Seat emptySeat = table.findEmptySeat();
        if (emptySeat == null) {
            this.msgMgr.sendEnterRoomMsg(player.getId(), Bjl.BjlCode.FULL_PLAYER);
            log.warn("玩家[{}][{}]进入的房间[{}]人数已满", player.getNickName(), player.getId(), table.name);
            return;
        }

        // 进入房间
        this.doEnterRoom(player, emptySeat);
    }

    /**
     * 进入房间
     *
     * @param player
     * @param seat
     */
    private void doEnterRoom(Player player, Seat seat) {
        Table table = seat.table;

        seat.playerId = player.getId();
        seat.nickName = player.getNickName();
        seat.online = true;
        seat.offlineRound = 0;
        seat.icon = player.getHeadImg();
        seat.sex = player.getSex();
        seat.gold = player.getGold();
        seat.robot = player.isRobot();

        // 更新玩家座位
        Cache.addSeat(seat);
        // 发送进入房间消息
        this.msgMgr.sendEnterRoomMsg(seat);
        this.msgMgr.sendOtherPlayerNumMsg(table);
        log.info("玩家[{}][{}]进入房间[{}]成功", player.getNickName(), player.getId(), table.name);
        if (!player.isRobot()) {
            this.playerService.syncRoom(table.id, table.id, player.getId());
            this.redisUtil.incr(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + table.id, 1.0);
            GameOnlineChangeUtils.incOnlineNum(this.gameId, table.id);
        } else {
            this.redisUtil.incr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + table.id, 1.0);
        }
    }

    /**
     * 退出房间
     *
     * @param playerId
     */
    public void exitRoom(Long playerId) {
        Seat seat = Cache.getSeat(playerId);
        if (seat == null) {
            log.warn("[百家乐] 玩家[{}]没在房间中 无法退出", playerId);
            return;
        }
        Table table = seat.table;
        if (seat.totalBet() > 0) {
            if (seat.robot) {
                seat.robotExit = true;
                log.warn("[百家乐] 房间[{}][{}] 机器人[{}], id:[{}]已下注，退出失败", table.name, table.id, seat.nickName, seat.playerId);
            } else {
                this.msgMgr.sendExitRoomMsg(seat, Bjl.BjlCode.BET_PLAYER_NOT_ALLOW_EXIT);
                log.warn("[百家乐] 房间[{}][{}] 玩家[{}], id:[{}]已下注，退出失败", table.name, table.id, seat.nickName, seat.playerId);
            }
            return;
        }
        if (seat == table.banker) {
            log.warn("[百家乐] 房间[{}][{}] 玩家[{}]为庄家，不能退出", table.name, table.id, seat.playerId);
            return;
        }

        log.info("[百家乐] 房间[{}][{}] 玩家[{}]退出游戏", table.name, table.id, seat.playerId);
        this.doExitRoom(Cache.getSeat(playerId));
    }

    /**
     * 退出房间
     *
     * @param seat
     */
    private void doExitRoom(Seat seat) {
        if (seat == null) {
            return;
        }
        Table table = seat.table;
        long playerId = seat.playerId;
        if (!seat.robot) {
            this.playerService.syncRoom(0, 0, seat.playerId);
            this.redisUtil.decr(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + table.id, 1.0);
            GameOnlineChangeUtils.decOnlineNum(this.gameId, table.id);
        } else {
            this.redisUtil.decr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + table.id, 1.0);
        }
        this.msgMgr.sendExitRoomMsg(seat, Bjl.BjlCode.SUCCESS);
        table.clearRankSeat(seat);
        Cache.removeSeat(playerId);
        log.info("玩家[{}][{}]退出房间[{}]", seat.playerId, seat.nickName, table.name);
        table.otherSeats.remove(seat);
        seat.clear();
        this.msgMgr.sendOtherPlayerNumMsg(table);
    }

    /**
     * 玩家下注
     *
     * @param playerId
     * @param area
     * @param gold
     */
    public void bet(Long playerId, int area, long gold) {
        Seat seat = Cache.getSeat(playerId);
        if (seat == null) {
            return;
        }
        Table table = seat.table;
        BjlTableWrapper roomBean = this.roomDic.get(seat.table.id);

        if ((area < 0) || (area >= Bjl.BetZoneType.ZONE_MAX.getNumber()) || (gold <= 0)) {
            log.warn("玩家[{}][{}]下注区域[{}]金币[{}]不合法", seat.nickName, seat.playerId, area, gold);
            return;
        }

        if (seat.gold < gold) {
            log.warn("玩家[{}][{}]金币[{}]不足下注金币[{}]", seat.nickName, seat.playerId, seat.gold, gold);
            return;
        }

        if (table.stage != GameStage.BET) {
            log.warn("非下注阶段玩家[{}][{}]不能下注", seat.nickName, seat.playerId);
            return;
        }

        if (seat.gold < roomBean.getChipList().get(0)) {
            this.msgMgr.sendPlayerAreaBetMsg(playerId, Bjl.BjlCode.BET_PLAYER_NOT_LOWER);
            log.warn("玩家[{}][{}]下注没有达到房间下注下限[{}]不能下注", seat.nickName, seat.playerId, gold, roomBean.getChipList().get(0));
            return;
        }

        if (!roomBean.getChipList().contains(gold)) {
            this.msgMgr.sendPlayerAreaBetMsg(playerId, Bjl.BjlCode.BET_GOLD_ERROR);
            log.warn("玩家[{}][{}]下注[{}]不符合下注筹码", seat.nickName, seat.playerId, gold);
            return;
        }

        if (gold > seat.gold) {
            this.msgMgr.sendPlayerAreaBetMsg(playerId, Bjl.BjlCode.BET_NOT_ENOUGH_GOLD);
            log.warn("玩家[{}][{}]金币不足筹码[{}]不能下注", seat.nickName, seat.playerId, gold);
            return;
        }

        long seatLimitGold = 0;
        seat.areaBets[area] += gold;// 预先加下注金币,计算限红
        seatLimitGold += seat.areaBets[0] * table.multiples.get(0);
        seatLimitGold += Math.abs(seat.areaBets[1] * table.multiples.get(1) - seat.areaBets[2] * table.multiples.get(2));
        seatLimitGold += seat.areaBets[3] * table.multiples.get(3);
        seatLimitGold += seat.areaBets[4] * table.multiples.get(4);
        seat.areaBets[area] -= gold;// 计算限红完成后减去刚加上的金币
        if (seatLimitGold > roomBean.getPlayerMaxBet()) {
            this.msgMgr.sendPlayerAreaBetMsg(playerId, Bjl.BjlCode.BET_SEAT_OVER);
            log.warn("玩家台红[{}],超出限制", seatLimitGold);
            return;
        }

        long tableLimitGold = 0;
        table.areaBets[area] += gold;// 预先加下注金币,计算限红
        tableLimitGold += table.areaBets[0] * table.multiples.get(0);
        tableLimitGold += Math.abs(table.areaBets[1] * table.multiples.get(1) - table.areaBets[2] * table.multiples.get(2));
        tableLimitGold += table.areaBets[3] * table.multiples.get(3);
        tableLimitGold += table.areaBets[4] * table.multiples.get(4);
        table.areaBets[area] -= gold;// 计算限红完成后减去刚加上的金币

        if (tableLimitGold > roomBean.getTableMaxBet()) {
            this.msgMgr.sendPlayerAreaBetMsg(playerId, Bjl.BjlCode.BET_AREA_OVER);
            log.warn("桌子台红[{}],超出限制", tableLimitGold);
            return;
        }
        this.doPlayerBet(seat, area, gold);
    }

    /**
     * 撤销下注
     *
     * @param playerId
     */
    public void revokeBet(Long playerId) {
        Seat seat = Cache.getSeat(playerId);
        Table table = seat.table;

        if (table.stage != GameStage.BET) {
            log.warn("非下注阶段玩家[{}][{}]不能撤销下注", seat.nickName, seat.playerId);
            return;
        }

        this.doRevokeBet(seat);
    }

    /**
     * 撤销下注
     *
     * @param seat
     */
    private void doRevokeBet(Seat seat) {
        Table table = seat.table;
        long preTotalBet = seat.totalBet();
        // 玩家区域下注
        long[] playerAreaBets = seat.areaBets;
        // 场次区域下注
        long[] tblAreaBets = table.areaBets;
        // 场次区域真实玩家下注
        long[] areaSeatBets = table.areaSeatBets;

        // 置为可续压状态
        seat.continueBeted = false;

        for (int i = 0; i < playerAreaBets.length; i++) {
            long betGold = playerAreaBets[i];
            seat.gold += betGold;
            table.totalBet -= betGold;
            tblAreaBets[i] -= betGold;
            playerAreaBets[i] = 0;
            if (!seat.robot) {
                areaSeatBets[i] -= betGold;
            }
        }

        for (int i = 0; i < table.playerBets.size(); i++) {
            table.playerBets.get(i).remove(seat.playerId);
        }

        this.msgMgr.sendRevokeBetMsg(seat, preTotalBet);
        this.msgMgr.sendSyncSeat(table, seat.playerId, seat.gold);
    }

    /**
     * 续压
     *
     * @param playerId
     * @param areaBets
     */
    public void continueBet(Long playerId, List<Bjl.AreaBetInfo> areaBets) {
        Seat seat = Cache.getSeat(playerId);
        if (seat == null || seat.continueBeted == true) {
            return;
        }
        Table table = seat.table;
        BjlTableWrapper roomBean = this.roomDic.get(seat.table.id);

        long totalBetGold = 0;

        if (table.stage != GameStage.BET) {
            log.warn("非下注阶段玩家[{}][{}]不能续压", seat.nickName, seat.playerId);
            return;
        }

        for (Bjl.AreaBetInfo areaBet : areaBets) {
            int area = areaBet.getArea();
            long gold = areaBet.getGold();
            if ((area < 0) || (area >= Bjl.BetZoneType.ZONE_MAX.getNumber()) || (gold <= 0)) {
                log.warn("玩家[{}][{}]续压区域[{}]金币[{}]不合法", seat.nickName, seat.playerId, area, gold);
                return;
            }

            if ((gold + seat.areaBets[area]) > roomBean.getPlayerMaxBet()) {
                this.msgMgr.sendPlayerAreaBetMsg(playerId, Bjl.BjlCode.BET_AREA_OVER);
                log.warn("房间下注总和[{}], 超出单区下注限制，下注：[{}]玩家[{}][{}]不能下注", gold + table.areaBets[area], gold, seat.nickName, seat.playerId);
                return;
            }
            totalBetGold += gold;
        }

        if (seat.gold < totalBetGold) {
            this.msgMgr.sendPlayerAreaBetMsg(playerId, Bjl.BjlCode.BET_NOT_ENOUGH_GOLD);
            log.warn("玩家[{}][{}]金币[{}]不足续压金币[{}]", seat.nickName, seat.playerId, seat.gold, totalBetGold);
            return;
        }

        for (Bjl.AreaBetInfo areaBet : areaBets) {
            this.doPlayerBet(seat, areaBet.getArea(), areaBet.getGold());
        }

        log.info("房间[{}], 桌[{}], 玩家[{}], 持续下注成功", table.id, table.id, seat.playerId);

        seat.continueBeted = true;
        this.msgMgr.sendContinueBetMsg(seat, Bjl.BjlCode.SUCCESS);
    }

    /**
     * 玩家下注
     *
     * @param seat
     * @param area
     * @param gold
     */
    private void doPlayerBet(Seat seat, int area, long gold) {
        Table table = seat.table;
        // 玩家区域下注
        long[] playerAreaBets = seat.areaBets;
        // 场次区域下注
        long[] tblAreaBets = table.areaBets;
        // 场次区域真实玩家下注
        long[] tblAreaSeatBets = table.areaSeatBets;
        seat.gold -= gold;
        table.totalBet += gold;
        playerAreaBets[area] += gold;
        tblAreaBets[area] += gold;

        // 下注玩家为真实玩家
        if (!seat.robot) {
            tblAreaSeatBets[area] += gold;
        }

        List<Long> listChip = table.betGoldCard.get(area); // 天地合下注筹码区
        if (listChip == null) {
            listChip = new LinkedList<>();
        }
        if (listChip.size() >= 30) {
            listChip.remove(0);
        }
        listChip.add(gold);

        Map<Long, List<Long>> mapBets = table.playerBets.get(area);
        List<Long> listBets = mapBets.get(seat.playerId);
        if (listBets == null) {
            listBets = Collections.synchronizedList(new LinkedList<>());
            mapBets.put(seat.playerId, listBets);
        }

        if (listBets.size() >= 30) {
            listBets.remove(0);
        }

        listBets.add(gold);

        // LOG.info("bets area:[{}], size:[{}]", area, listChip.size() );

        boolean bool = table.rankSeats.contains(seat);
        if (!bool) {
            table.isBet = true;

            List<Long> listSeat = table.noSeatCard.get(area);
            if (listSeat == null) {
                listSeat = new LinkedList<>();
                table.noSeatCard.put(area, listSeat);
            }
            List<Long> listPlayer = table.noSeatPlayerId.get(area);
            if (listPlayer == null) {
                listPlayer = new LinkedList<>();
                table.noSeatPlayerId.put(area, listPlayer);
            }
            if (listSeat.size() > 30) {
                listSeat.remove(0);
                listPlayer.remove(0);
            }
            listSeat.add(gold);
            listPlayer.add(seat.playerId);
        }
        // 发送下注成功
        this.msgMgr.sendPlayerAreaBetMsg(seat, area, gold);
        this.msgMgr.sendGoldChangeMsg(seat);
        this.msgMgr.sendSyncSeat(table, seat.playerId, seat.gold);
        if (!seat.robot) {
            log.info("玩家[{}][{}]下注区域[{}][{}]成功", seat.nickName, seat.playerId, area, gold);
        }
        Player player = this.playerService.getPlayer(seat.playerId);
        if (player == null) {
            log.error("玩家不存在:[{}]", seat.playerId);
        }
        player.setGold(player.getGold() - gold);
        this.playerService.updatePlayer(player);

        if (table.godOfGamblersFirstBetTable < 0) {
            if (table.rankSeats.size() >= 2) {
                Seat sss = table.rankSeats.get(1);
                if (seat == sss) {
                    table.godOfGamblersFirstBetTable = area;
                }
            }
        }
    }

    /**
     * 同步玩家战绩 牌局明细
     */
    private void saveTableRecord(Table table) {
        try {
            // 上局赢家
            List<SeatPlayer> brnnSeatPlayers = table.brnnTableRecord.seatWinPlayer;
            brnnSeatPlayers.clear(); // 清空上局赢家
            if (table.totalBet <= 0) {
                return;
            }

            // 玩家排序
            table.seats.sort(new Comparator<Seat>() {
                @Override
                public int compare(Seat o1, Seat o2) {
                    return Long.compare(o2.totalWinGold() - o2.totalBet(), o1.totalWinGold() - o1.totalBet());
                }
            }); // 排序 赢家玩家列表
            int number = table.seats.size() > 50 ? 50 : table.seats.size();
            for (int i = 0; i < number; i++) {
                Seat seat = table.seats.get(i);
                if ((seat.playerId > 0) && (seat.totalWinGold() - seat.totalBet() > 0)) {
                    SeatPlayer seatPlayer = new SeatPlayer();
                    seatPlayer.setGold(seat.totalWinGold() - seat.totalBet());
                    seatPlayer.setIcon(seat.icon);
                    seatPlayer.setNickName(seat.nickName);
                    seatPlayer.setPlayerId(seat.playerId);
                    brnnSeatPlayers.add(seatPlayer); // 上局 50条赢家记录
                }
            }
        } catch (Exception ex) {
            log.error("同步玩家战绩 牌局明细 异常[{}]", ex);
        }
    }

    /**
     * 战绩 我的明细 最新50条
     */
    private void saveSeatRecord(Seat seat) {
        try {
            Table table = seat.table;
            SeatRecord record = new SeatRecord();
            record.gameOverTime = (int) (System.currentTimeMillis() / 1000);
            record.roomType = "bjl" + table.name; // 房间名称+场次
            long totalGold = seat.totalWinGold();
            record.gold = seat.gold + totalGold;
            record.betStartGold = seat.gold + seat.totalBet();
            record.winLoseGold = totalGold - seat.totalBet();
            record.bankerCards = table.bankerCards();
            record.playerCards = table.playerCards();

            if (seat == seat.table.banker) {
                record.isBanker = true;
                record.areaBets = this.intsConvert(record.areaBets, table.areaBets);
                record.areaWinGold = this.intsConvert(record.areaWinGold, seat.winGolds);
            } else {
                record.areaBets = this.intsConvert(record.areaBets, seat.areaBets);
                record.isBanker = false;
                record.areaWinGold = this.intsConvert(record.areaWinGold, seat.winGolds);
            }

            seat.seatRecord = record;
        } catch (Exception ex) {
            log.warn("异常", ex);
        }
    }

    /**
     * 牌局明细 我的明细
     */
    public void onSeatDetailed(Long playerId) {
        this.msgMgr.sendSeatDetailed(playerId, this.gameHistory.queryGameHistory(playerId));
    }

    /**
     * 牌局明细 盈利榜
     */
    public void onProfitList(Long playerId) {
        Seat seat = Cache.getSeat(playerId);
        if (seat == null) {
            log.warn("[百家乐] 玩家[{}]没在房间中 无法查看盈利榜", playerId);
            return;
        }
        Table table = seat.table;

        long beg = System.currentTimeMillis();

        Bjl.ResProfitList.Builder msg = null;
        // 短时间重复请求，提升响应性能
        if (table.profitListChanged) {
            msg = Bjl.ResProfitList.newBuilder();
            msg.setOverTime(0);
            Set<ZSetOperations.TypedTuple<Long>> ranks = this.gameHistory.queryRankProfit(table.id, table.id, 0, 20); // 盈利榜历史 最新20条
            for (ZSetOperations.TypedTuple<Long> tt : ranks) {
                Double dayGoldTotal = tt.getScore();
                Object playerIdObj = tt.getValue();
                if (playerIdObj == null) {
                    continue;
                }

                Integer tPlayerId = (Integer) playerIdObj;
                if (dayGoldTotal != null && dayGoldTotal > 0) {
                    Bjl.SeatInfoRecord.Builder record = Bjl.SeatInfoRecord.newBuilder();

                    Player p = this.playerService.getPlayer(tPlayerId);
                    record.setNickName(p.getNickName());
                    record.setGold((long) (dayGoldTotal.doubleValue() * 100));
                    record.setIcon(p.getHeadImg());
                    record.setPlayerId(tPlayerId);
                    msg.addSeatRecord(record);
                }
            }
            table.profitListChanged = false;
            table.lastProfitList = msg;
        } else {
            msg = table.lastProfitList;
        }

        log.info("玩家[{}]获取今日盈利数据排行 耗时:[{}] 毫秒", playerId, (System.currentTimeMillis() - beg));
        this.msgMgr.sendProfitList(seat.playerId, msg);
    }

    /**
     * 牌局明细 上局赢家
     */
    public void onSeatWin(Long playerId) {
        Seat seat = Cache.getSeat(playerId);
        if (seat == null) {
            log.warn("[百家乐] 玩家[{}]没在房间中 无法查看上局赢家", playerId);
            return;
        }
        this.msgMgr.sendSeatWin(seat);
    }

    /**
     * 数组 长度转换
     */
    private long[] intsConvert(long[] betGold, long[] areaBets) {
        for (int i = 0; i < areaBets.length; i++) {
            betGold[i] = areaBets[i];
        }
        return betGold;
    }

    /**
     * 结算庄家以外的玩家
     */
    private void balanceSeat(Table table) {
        List<Seat> seats = table.seats;

        List<Seat> seatsForSaveRecord = new ArrayList<>();

        Map<String, Object> gameResult = new HashMap<>();
        Map<String, Object> cardType = new HashMap<>();
        gameResult.put("牌型", cardType);
        cardType.put("庄牌", table.bankerCards());
        cardType.put("闲牌", table.playerCards());
        cardType.put("赢家", table.winner.getNumber());
        Map<String, Map<String, Long>> details = new HashMap<>();
        gameResult.put("输赢详情", details);
        List<Integer> multi = Cache.getAreaMultiples();

        int[] index = { 1, 2, 0, 3, 4 };
        for (int i : index) {
            Map<String, Long> map = new HashMap<>();
            details.put("位置-" + i, map);
            map.put("倍数", (long) multi.get(i));
            map.put("下注", 0L);
            map.put("赔付", 0L);
        }

        for (Seat seat : seats) { // 遍历座位
            if (seat.playerId <= 0) {
                continue;
            }
            if (seat == table.banker) { // 庄家不在这里计算
                continue;
            }
            if (!seat.online) { // 掉线
                seat.offlineRound++;
            }
            long totalBet = seat.totalBet();
            if (totalBet <= 0) { // 未下注
                seat.notBetRound++;
            } else {
                seat.notBetRound = 0;
                long totalWinGold = seat.totalWinGold();

                if (totalWinGold > 0) {
                    // 此处已经扣税 直接同步到数据库
                    seat.gold = seat.gold + totalWinGold;
                }
                if (!seat.robot) {
                    // 此处已经扣税 直接同步到数据库
                    this.playerService.settle(seat.playerId, totalWinGold - totalBet);

                    BigDecimal changeGold = new BigDecimal(totalWinGold - totalBet + seat.tax).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    if (changeGold.compareTo(BigDecimal.ZERO) > 0) {
                        this.redisUtil.decr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + table.id, changeGold.doubleValue());
                    } else {
                        this.redisUtil.incr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + table.id, changeGold.negate().doubleValue());
                    }
                }
                for (int i = 0; i < Bjl.BetZoneType.ZONE_MAX.getNumber(); i++) {
                    Map<String, Long> map = details.get("位置-" + i);
                    map.put("下注", map.get("下注") + seat.areaBets[i]);
                    map.put("赔付", map.get("赔付") + seat.winGolds[i]);
                }
                seatsForSaveRecord.add(seat);
            }
        }

        String roundCode = RoundIdUtils.getGameRecordId(String.valueOf(this.gameId), String.valueOf(table.id));
        for (Seat seat : seatsForSaveRecord) {
            this.doSaveRecord(seat, gameResult, roundCode);
        }
    }

    /**
     * 保存战绩
     */
    private void doSaveRecord(Seat seat, Map<String, Object> gameResult, String roundCode) {
        long totalBet = seat.totalBet();
        long totalWinGold = seat.totalWinGold();

        Table table = seat.table;
        BjlTableWrapper config = this.roomDic.get(table.id);

        BigDecimal beforeGameGold = new BigDecimal(seat.gold - totalWinGold + totalBet).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal afterGameGold = new BigDecimal(seat.gold).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal winLosGold = new BigDecimal(totalWinGold - totalBet).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal betsGold = new BigDecimal(totalBet).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);

        Date gameDate = new Date();
        GameRecordDTOBuilder<Map<String, Object>> build = GameRecordDTO.builder();
        build.gameDate(gameDate);// 游戏牌局时间
        build.userId(seat.playerId);// 用户id
        build.userName(seat.nickName);// 用户昵称
        build.gameId(this.gameId);// 游戏id

        build.fileId(config.getId());// 场次id
        build.fileName(config.getName());// 场次名称

        build.beforeGameGold(beforeGameGold);// 游戏前金币
        build.afterGameGold(afterGameGold);// 游戏后金币
        build.betsGold(betsGold);// 下注金币
        build.winLosGold(winLosGold);// 输赢金币
        build.serviceCharge(new BigDecimal(seat.tax).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP));// 服务费

        List<Map<String, Object>> gameResults = new ArrayList<>();
        gameResults.add(gameResult);
        build.gameResult(gameResults);

        build.isRobot(seat.robot);// 是否是机器人
        build.roundCode(roundCode);
        try {
            if (table.playerNum() == 0) {
                return;
            }
            this.defautProducer.sendAsync(JSON.toJSONString(build.build()));
        } catch (Exception e) {
            log.error("战绩信息发送异常:" + seat.playerId, e);
        }
    }

    /**
     * 添加线路图
     *
     * @param table
     * @return
     */
    private Bjl.RouteInfo addRoute(Table table) {
        Bjl.RouteInfo.Builder rtb = Bjl.RouteInfo.newBuilder();
        rtb.setCardInfo(this.getHandCardInfo(table));
        rtb.setItems(table.winner.getNumber());

        Bjl.RouteInfo route = rtb.build();
        table.routes.add(route);
        this.saveRouteToRedis(table, route);
        return route;
    }

    private void saveRouteToRedis(Table table, Bjl.RouteInfo route) {
        if (!this.roomDic.get(table.id).isOpen()) {
            return;
        }
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YY-MM-dd");
        String date = sdf.format(now);
        String key = String.format("%s:route:room_%d:%s", "bjl", table.id, date);
        String jsonStr = JsonFormat.printToString(route);
        JSONObject json = JSON.parseObject(jsonStr);
        if (json.get("items") != null) {
            int win = Integer.parseInt(json.get("items").toString());
            if (win == 0) {
                json.put("items", "和");
            } else if (win == 1) {
                json.put("items", "庄");
            } else if (win == 2) {
                json.put("items", "闲");
            }
        }
        json.putAll(json.getJSONObject("cardInfo"));
        json.remove("cardInfo");
        this.stringRedisTemplate.opsForList().rightPush(key, JSON.toJSONString(json));
    }

    /**
     * 清空线路图
     *
     * @param table
     * @return
     */
    private void clearRoutes(Table table) {
        table.routes.clear();
    }

    public void getTableCardInfo(Table table, Bjl.CardGroup.Builder bcg, Bjl.CardGroup.Builder pcg) {
        int[] bc = table.bankerCards();
        for (int c : bc) {
            bcg.addCard(c);
        }

        int[] pc = table.playerCards();
        for (int c : pc) {
            pcg.addCard(c);
        }
    }

    public Bjl.HandCardInfo getHandCardInfo(Table table) {
        Bjl.HandCardInfo.Builder b = Bjl.HandCardInfo.newBuilder();
        Bjl.CardGroup.Builder bcg = Bjl.CardGroup.newBuilder();
        Bjl.CardGroup.Builder pcg = Bjl.CardGroup.newBuilder();
        this.getTableCardInfo(table, bcg, pcg);
        b.addCardGroup(bcg);
        b.addCardGroup(pcg);
        return b.build();
    }

    /**
     * 金币充值
     */
    public void goldPay(Long playerId, long payGold) {
        Seat seat = Cache.getSeat(playerId);
        if (seat != null) {
            seat.gold += payGold;
            this.msgMgr.sendGoldPayMsg(seat, payGold);
            Player player = this.playerService.getPlayer(playerId);
            if (player != null) {
                player.setGold(player.getGold() + payGold);
                this.playerService.updatePlayer(player);
            }
        } else {
            Player player = this.playerService.getPlayer(playerId);
            if (player != null) {
                player.setGold(player.getGold() + payGold);
                this.playerService.updatePlayer(player);
                this.msgMgr.sendGoldPayMsg(playerId, player.getGold(), payGold);
            }
        }
    }

    /**
     * 掉线
     */
    public void lostOnline(Long playerId) {
        Seat seat = Cache.getSeat(playerId);
        if (seat != null) {
            seat.online = false;
            this.exitRoom(playerId);
        }
    }

    /**
     * 停服
     */
    public void stopService() {
        this.STOP_SERVICE = true;
    }

    public Boolean getStopService() {
        return this.STOP_SERVICE;
    }
}