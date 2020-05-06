package com.dmg.doudizhuserver.business.logic;

import static com.dmg.doudizhuserver.business.model.CardsType.ZHA_DAN;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.util.SpringUtil;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.doudizhuserver.business.ai.DdzGoodCardPool;
import com.dmg.doudizhuserver.business.ai.RobotAI;
import com.dmg.doudizhuserver.business.config.local.ConfigService;
import com.dmg.doudizhuserver.business.config.local.Constant;
import com.dmg.doudizhuserver.business.config.local.RoomConfig;
import com.dmg.doudizhuserver.business.model.Card;
import com.dmg.doudizhuserver.business.model.CardsType;
import com.dmg.doudizhuserver.business.model.DdzCache;
import com.dmg.doudizhuserver.business.model.HandCards;
import com.dmg.doudizhuserver.business.model.PlayCards;
import com.dmg.doudizhuserver.business.model.PlayerSettle;
import com.dmg.doudizhuserver.business.model.Room;
import com.dmg.doudizhuserver.business.model.Seat;
import com.dmg.doudizhuserver.business.model.Stage;
import com.dmg.doudizhuserver.business.util.CardsTypeComparator;
import com.dmg.doudizhuserver.business.util.CardsTypeFinder;
import com.dmg.doudizhuserver.common.platform.model.Player;
import com.dmg.doudizhuserver.common.platform.model.Robot;
import com.dmg.doudizhuserver.common.platform.service.PlayerService;
import com.dmg.doudizhuserver.core.net.socket.SocketMgr;
import com.dmg.doudizhuserver.core.net.socket.SocketServer;
import com.dmg.doudizhuserver.core.work.Worker;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.model.dto.GameRecordDTO.GameRecordDTOBuilder;
import com.dmg.server.common.util.RoundIdUtils;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.log4j.Log4j2;

/**
 * 实现逻辑
 */
@Log4j2
@Service
public class RealizeLogic {
    @Value("${game-id}")
    private int gameId;
    @Autowired
    private ResLogic resLogic;
    @Autowired
    private Worker worker;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MQProducer defautProducer;

    /** 停服标注 */
    private boolean STOP_SERVICE;

    @PostConstruct
    private void init() {
        this.heartbeatLostTest();
    }

    /** 玩家进入房间 */
    public synchronized void enterRoom(long playerId, int roomId) {
        Player player = this.playerService.getPlayerPlatform(playerId);
        if (player == null) {
            String error = MessageFormat.format("玩家[{0}]不存在", playerId);
            log.error(error);
            this.resLogic.sendErrorMsg(playerId, "EnterRoom", 1, error);
            return;
        }
        Seat seat = DdzCache.playerSeats.get(playerId);
        if (seat != null) {// 断线重连
            seat.online = true;
            seat.heartbeatLost = false;
            this.resLogic.sendRestoreMsg(seat);// 发送恢复现场消息
            log.info("玩家[{}][{}]恢复现场进入房间[{}]成功", seat.nickname, seat.playerId, seat.room.config.name);
            if (seat.auto && !seat.autoActive) {// 断线重连如果有主动托管，无需取消
                this.doAutoPlay(seat.playerId, false, false);
            }
            return;
        } else {
            if (this.STOP_SERVICE) {
                String error = MessageFormat.format("服务器停止,玩家[{}]不能进入", playerId);
                log.error(error);
                this.resLogic.sendErrorMsg(playerId, "EnterRoom", 99, error);
                return;
            }
            RoomConfig config = this.configService.getRoomConfig(roomId);
            if (config == null) {
                String error = "玩家进入的房间不存在!";
                log.error(error);
                this.resLogic.sendErrorMsg(playerId, "EnterRoom", 2, error);
                return;
            }
            if (!config.isOpen()) {
                String error = MessageFormat.format("房间[{0}]未开放", config.name);
                log.error(error);
                this.resLogic.sendErrorMsg(playerId, "EnterRoom", 3, error);
                return;
            }
            int playerNum = 0;
            String playerNumStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + config.getLevel());
            if (StringUtils.isNotBlank(playerNumStr)) {
                playerNum = Integer.parseInt(playerNumStr);
            }
            if (playerNum > config.getPlayerUpLimit()) {
                String error = MessageFormat.format("房间[{0}]已满", config.name);
                log.error(error);
                this.resLogic.sendErrorMsg(playerId, "EnterRoom", 4, error);
                return;
            }
            if (player != null && player.getGold().compareTo(config.getEnterScore()) < 0) {// 判断金币是否足够进入房间
                String error = MessageFormat.format("玩家[{0}][{1}]进入的房间[{2}]低于下限[{3}]金币", player.getNickname(), playerId, config.getName(), config.getEnterScore());
                log.error(error);
                this.resLogic.sendErrorMsg(playerId, "EnterRoom", 5, error);
                return;
            }

            List<Room> curNotStartRooms = DdzCache.curNotStartRooms.get(config.getLevel());
            if (curNotStartRooms == null) {
                curNotStartRooms = new ArrayList<>();
                DdzCache.curNotStartRooms.put(config.getLevel(), curNotStartRooms);
            }
            for (Room room : curNotStartRooms) {
                if (seat != null) {
                    break;
                }
                for (Seat tseat : room.seats) {
                    if (tseat.playerId != 0) {
                        continue;
                    }
                    seat = tseat;
                    break;
                }
            }
            if (seat == null) {
                Room room = new Room(config);
                curNotStartRooms.add(room);
                seat = room.seats.get(0);
                this.redisUtil.incr(RedisRegionConfig.FILE_ROOM_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
            }
            this.enterRoom(player, seat);
        }
    }

    /** 进入房间 */
    private void enterRoom(Player player, Seat seat) {
        Room room = seat.room;
        RoomConfig config = room.config;
        seat.playerId = player.getId();
        seat.nickname = player.getNickname();
        seat.headImg = player.getHeadImg();
        seat.sex = player.getSex();
        seat.robot = player.isRobot();
        seat.online = true;
        seat.heartbeatLost = false;
        DdzCache.playerSeats.put(player.getId(), seat);// 更新玩家座位
        this.resLogic.sendEnterRoomMsg(seat); // 发送进入房间消息
        if (!player.isRobot()) {
            this.playerService.syncRoom(config.level, room.uniquId, player.getId());
            this.redisUtil.incr(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
        } else {
            this.redisUtil.incr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
        }
        log.info("玩家[{}][{}]进入房间[{}][{}]成功", seat.nickname, seat.playerId, config.name, room.uniquId);
        if (room.isSeatFull()) {
            synchronized (room) {
                room.stage = Stage.DEAL_CARDS;// 进入准备发牌阶段
                DdzCache.curNotStartRooms.get(config.getLevel()).remove(room);
                this.resLogic.sendWillDealCardsMsg(room);
                this.worker.schedule(() -> {
                    this.doDealCards(room);
                }, 2, TimeUnit.SECONDS);
            }
        } else if (room.robotFuture == null || room.robotFuture.isDone()) {
            log.info("房间[{}][{}]准备开始匹配机器人", config.name, room.uniquId);
            room.robotFuture = this.worker.schedule(() -> {
                log.info("房间[{}][{}]开始匹配机器人", config.name, room.uniquId);
                List<Seat> seats = room.seats;
                for (Seat tseat : seats) {
                    if (tseat.playerId != 0) {
                        continue;
                    }
                    Robot robot = null;
                    int nn = 0;
                    while (true) {
                        long robotId = RandomUtil.randomInt(300) + 1;
                        robotId = robotId + 10000000 + 101000;
                        if (DdzCache.playerSeats.get(robotId) != null) {
                            log.info("房间[{}][{}]机器人id[{}]冲突，重新查找", config.name, room.uniquId, robotId);
                            continue;
                        }
                        robot = this.playerService.getRobot(robotId, config.getEnterScore(), config.getEnterScore().multiply(BigDecimal.TEN));
                        if (robot != null) {
                            break;
                        }
                        log.info("房间[{}][{}]机器人id[{}]未找到", config.name, room.uniquId, robotId);
                        if (++nn > 100) {
                            log.info("房间[{}][{}]机器人匹配陷入无限循环中", config.name, room.uniquId);
                        }
                    }
                    log.info("房间[{}][{}]加入机器人[{}][{}]", config.name, room.uniquId, robot.getId(), robot.getNickname());
                    this.enterRoom(robot, tseat);
                }
                log.info("房间[{}][{}]匹配机器人完成", config.name, room.uniquId);
            }, Constant.ROBOT_ENTER_TIME, TimeUnit.SECONDS);
        }
    }

    /** 发牌,每人先发17张 */
    private void doDealCards(Room room) {
        List<Card> cards = room.cards;
        boolean HAO_PAI = false;
//        int rand = RobotAI.randInt(100);
//        if (rand < 15) {
//            HAO_PAI = true;
//        }
        HAO_PAI = false;
        if (HAO_PAI) {
            List<String> goods = new ArrayList<>();
            List<String> nomals = new ArrayList<>();
            int count = 0;
            for (Seat s : room.seats) {
                if (s.robot && count == 0) {
                    goods.add(String.valueOf(s.playerId));
                    count++;
                } else {
                    nomals.add(String.valueOf(s.playerId));
                }
            }
            Map<String, List<Card>> map = DdzGoodCardPool.createCards(goods, nomals);
            for (Seat s : room.seats) {
                s.handCards = new HandCards(map.get(String.valueOf(s.playerId)));
            }
        } else {
            int cardIndex = 0;
            for (Seat s : room.seats) {
                s.handCards = new HandCards(cards.subList(cardIndex, cardIndex += Constant.FARMER_CARDS));
            }
        }

        for (Seat s : room.seats) {
            log.info("发送玩家[{}][{}]牌[{}]", s.nickname, s.playerId, s.handCards.printCardStr());
        }

        room.initHiddenCards();
        log.info("地主牌{}", room.hiddenCards);

        Seat seat = room.seats.get(RandomUtil.randomInt(room.seats.size()));
        // 随机叫牌的位置
        room.nextCallSeat = seat;
        log.warn("玩家[{}]开始叫分", seat.nickname);
        // 向同桌的玩家发送牌消息
        this.resLogic.sendDealCardsMsg(room);

        room.stage = Stage.CALL_LANDLORD;// 进入叫地主阶段

        // 第一个玩家叫牌要加上发牌动画时间
        int time = Constant.CALL_TIME + Constant.PLAYER_ACTION_DELAY + Constant.DEAL_CARDS_TIME;
        if (seat.robot) {
            time = RandomUtil.randomInt(3, Constant.CALL_TIME / 3 + 1);
            time = time + Constant.PLAYER_ACTION_DELAY + Constant.DEAL_CARDS_TIME;
        }
        // 超时不抢地主
        room.future = this.worker.schedule(() -> {
            int call = 0;
            if (seat.robot) {
                call = RandomUtil.randomInt(2, 3 + 1);
            }
            final int call0 = call;
            this.doCallLandlord(seat, call0);
        }, time, TimeUnit.SECONDS);

    }

    /**
     * 玩家叫地主
     *
     * @param playerId
     * @param call
     */
    public void callLandlord(long playerId, int call) {
        Seat seat = DdzCache.playerSeats.get(playerId);
        Room room = seat.room;
        if (room.stage != Stage.CALL_LANDLORD) {
            log.warn("玩家[{}][{}]不能叫地主,当前是[{}]阶段", seat.nickname, seat.playerId, room.stage);
            return;
        }
        Seat nextCallSeat = room.nextCallSeat;
        if (nextCallSeat != seat) {
            log.error("玩家[{}][{}]不能叫地主,当前应该[{}][{}]叫地主", seat.nickname, seat.playerId, nextCallSeat.nickname, nextCallSeat.playerId);
            return;
        }
        if (call != 0 && room.landlord != null && room.landlord.callScore >= call) {// 玩家叫分小于等于当前地主叫分
            log.error("玩家[{}][{}]叫地主[{}]小于当前地主[{}]", seat.nickname, seat.playerId, nextCallSeat.nickname, call, room.landlord.callScore);
            return;
        }
        this.doCallLandlord(seat, call);
    }

    /**
     * 叫地主
     */
    private void doCallLandlord(Seat seat, int call) {
        Room room = seat.room;
        room.closeFuture(false);
        log.warn("{}[{}]叫分[{}]", seat.robot ? "机器人" : "玩家", seat.nickname, call);
        if (call > 0) {
            seat.callScore = call;
            if (room.landlord == null || room.landlord.callScore < call) {
                room.landlord = seat;
            }
        } else {
            seat.callScore = -1;
        }

        if (call == 3) {// 叫地主直接结束
            room.nextCallSeat = null;
            this.resLogic.sendCallLandlordMsg(seat, call);
            this.callLandlordOver(room);
            return;
        }

        Seat nextCallSeat = room.nextSeat(seat);// 下一个叫牌的玩家,叫地主分数不为0为叫地主结束
        if (nextCallSeat.callScore == 0) {
            room.nextCallSeat = nextCallSeat;
            this.resLogic.sendCallLandlordMsg(seat, call);
            int time = Constant.CALL_TIME + Constant.PLAYER_ACTION_DELAY;
            if (nextCallSeat.robot) {
                time = RandomUtil.randomInt(Constant.PLAYER_ACTION_DELAY, time / 3 + 1);
            }
            room.future = this.worker.schedule(() -> { // 超时不叫地主
                int random = 0;
                if (nextCallSeat.robot) {
                    if (seat.robot) {
                        if (seat.robot) {
                            random = RandomUtil.randomInt(2, 3 + 1);
                        }
                    }
                    if (random != 0 && room.landlord != null && room.landlord.callScore >= random) {
                        random = 0;
                    }
                }
                final int call0 = random;
                this.doCallLandlord(nextCallSeat, call0);
            }, time, TimeUnit.SECONDS);
            return;
        }
        room.nextCallSeat = null;
        this.resLogic.sendCallLandlordMsg(seat, call);
        boolean restart = room.allNotCallLandlord();
        if (restart) { // 重新开始
            for (Seat s : room.seats) {
                s.reset();
            }

            room.landlord = null;
            room.nextCallSeat = null;
            room.prePlayCards = null;
            room.closeFuture(true);
            room.closeRobotFuture();
            room.closeFuture0();

            this.doDealCards(room);
        } else {
            this.callLandlordOver(room);
        }
    }

    /**
     * 叫地主结束
     *
     * @param room
     */
    private void callLandlordOver(Room room) {
        room.closeFuture(false);
        room.stage = Stage.GAME;// 进入游戏阶段
        Seat landlord = room.landlord;
        room.nextPlaySeat = landlord;
        room.prePlaySeat = landlord;
        // 将底牌放入自己的牌中
        landlord.handCards.cards.addAll(room.hiddenCards);
        // 叫牌结束 发送底牌
        this.resLogic.sendHiddenCardsMsg(room);
        // 向同桌的玩家发送牌消息
        this.resLogic.sendDealCardsMsg(landlord);
        log.info("房间[{}]叫分结束,玩家[{}][{}]是地主", room.uniquId, landlord.nickname, landlord.playerId);

        // 地主出牌
        this.scheduleAutoPlayCards(room.landlord);

        room.future00 = this.worker.scheduleWithFixedDelay(() -> {
            boolean flag = false;
            for (Seat s : room.seats) {
                long i = 0;
                if (s.future != null) {
                    i = s.future.getDelay(TimeUnit.SECONDS);
                }

                if (i > -10) {
                    flag = true;
                    break;
                }
            }
            if (flag == true) {
                return;
            }
            Seat seat = room.nextPlaySeat;
            if (seat.future != null && seat.future.getDelay(TimeUnit.SECONDS) > 0) {
                return;
            }
            List<Integer> cardIds = new ArrayList<>();
            if (room.prePlaySeat == seat) {
                cardIds.add(seat.handCards.getSortCards().get(0).id);
            }
            boolean flag2 = this.playCards(seat.playerId, cardIds);
            if (flag2 == false) {
                log.error("出牌错误,无法纠正--------------------------------------------------------------------------------------------------------");
            }
        }, 10, 5, TimeUnit.SECONDS);
    }

    /**
     * schedule玩家自动出牌
     *
     * @param seat
     */
    private void scheduleAutoPlayCards(Seat seat) {
        this.scheduleAutoPlayCardsTable(seat);
        this.doAutoPlayCardAuto(seat, false);
        this.doAutoPlayCardRobot(seat);
    }

    /** 桌面定时器超时出牌 */
    private void scheduleAutoPlayCardsTable(Seat seat) {
        seat.future = this.worker.schedule(() -> {
            try {
                Room room = seat.room;
                List<Card> cards = new ArrayList<>();
                // 上一次出牌也是我，即是第一手出牌
                if (room.prePlaySeat == seat) {
                    PlayCards pc = new PlayCards(CardsTypeFinder.findMinCards(seat.handCards.cards));
                    cards = pc.cards;
                }
                List<Integer> cardIds = new ArrayList<>();
                for (Card tcard : cards) {
                    cardIds.add(tcard.id);
                }
                boolean flag = this.playCards(seat.playerId, cardIds);
                if (flag == false) {
                    this.scheduleAutoPlayCardsError(seat, room, cards);
                }
                if (!seat.robot) {
                    if ((++seat.autoNum) >= 1) {
                        seat.autoNum = 0;
                        this.doAutoPlay(seat.playerId, true, false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, Constant.PLAY_TIME + Constant.PLAYER_ACTION_DELAY, TimeUnit.SECONDS);
    }

    /** 托管出牌 */
    private void doAutoPlayCardAuto(Seat seat, boolean promptly) {
        Room room = seat.room;
        if (seat != room.nextPlaySeat) {
            return;
        }
        if (seat.auto) {
            int playTime = Constant.AUTO_PLAY_TIME + Constant.PLAYER_ACTION_DELAY;
            if (promptly) {
                playTime = 0;
            }
            seat.autoFuture = this.worker.schedule(() -> {

                List<Card> cards = new ArrayList<>();
                // 上一次出牌也是我，即是第一手出牌
                if (room.prePlaySeat == seat) {
                    PlayCards pc = new PlayCards(CardsTypeFinder.findMinCards(seat.handCards.cards));
                    cards = pc.cards;
                } else {
                    PlayCards pc = CardsTypeFinder.findBiggerCards(seat.handCards, room.prePlayCards);
                    if (pc != null) {
                        cards = pc.cards;
                    }
                }
                List<Integer> cardIds = new ArrayList<>();
                for (Card tcard : cards) {
                    cardIds.add(tcard.id);
                }
                boolean flag = this.playCards(seat.playerId, cardIds);
                if (flag == false) {
                    this.scheduleAutoPlayCardsError(seat, room, cards);
                }
            }, playTime, TimeUnit.SECONDS);
        } else {
            seat.closeAutoFuture(false);
        }
    }

    /** 机器人出牌 */
    private void doAutoPlayCardRobot(Seat seat) {
        if (!seat.robot) {
            return;
        }
        Room room = seat.room;
        List<Card> cards = RobotAI.getPlayCard(seat);

        int playTime = 0;
        if (!cards.isEmpty()) {
            if (cards.size() < 2) {
                playTime = RandomUtil.randomInt(1, 4);
            } else if (cards.size() < 10) {
                playTime = RandomUtil.randomInt(2, 7);
            } else {
                playTime = RandomUtil.randomInt(3, 9);
            }
        } else {
            playTime = 1;
        }
        if (cards.size() == seat.handCards.cards.size()) {
            playTime = 1;
        }

        List<Card> cardt = new ArrayList<>(cards);
        seat.robotFuture = this.worker.schedule(() -> {
            try {
                List<Integer> cardIds = new ArrayList<>();
                for (Card tcard : cardt) {
                    cardIds.add(tcard.id);
                }
                boolean flag = this.playCards(seat.playerId, cardIds);
                if (flag == false) {
                    this.scheduleAutoPlayCardsError(seat, room, cardt);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, playTime + Constant.PLAYER_ACTION_DELAY, TimeUnit.SECONDS);
    }

    private void scheduleAutoPlayCardsError(Seat seat, Room room, List<Card> cards) {
        if (seat.playerId == 0) {
            log.error("座位错误");
        } else {
            log.error("出牌错误--------------------------------------------------------------------------------------------------------");
            PlayCards p = new PlayCards(cards);
            if (room.prePlaySeat == seat) {
                log.error("上次出牌无,当前出牌[{}][{}]{},当前手牌{}", seat.playerId, seat.nickname, p.sortedCards, seat.handCards.printCardStr());
            } else {
                log.error("上次出牌[{}][{}]{},当前出牌[{}][{}]{},当前手牌{}", room.prePlaySeat.playerId, room.prePlaySeat.nickname, room.prePlayCards, seat.playerId, seat.nickname, p.sortedCards, seat.handCards.printCardStr());
            }
        }
    }

    /**
     * 玩家出牌
     *
     * @param playerId
     * @param cardIds
     */
    public boolean playCards(long playerId, List<Integer> cardIds) {
        Seat seat = DdzCache.playerSeats.get(playerId);
        if (seat == null) {
            log.warn("座位空[{}]", "playCards");
            return false;
        }
        Room room = seat.room;
        if (seat != room.nextPlaySeat) {
            log.warn("现在不该玩家[{}][{}]出牌", seat.nickname, seat.playerId);
            return false;
        }
        if (cardIds == null || cardIds.size() == 0) { // 放弃出牌
            return this.doGiveUp(seat);
        }
        if ((cardIds.size() == 0) || (cardIds.size() > 20)) {
            log.warn("玩家[{}][{}]出的牌数量[{}]不正确", seat.nickname, seat.playerId, cardIds.size());
            return false;
        }
        List<Card> cards = new ArrayList<>();
        for (Integer cardId : cardIds) {
            Card card = Card.getCard(cardId);
            if ((card == null) || !seat.handCards.cards.contains(card)) {
                log.warn("玩家[{}][{}]手上没有该牌[{}]", seat.nickname, seat.playerId, card);
                return false;
            }
            cards.add(card);
        }

        // 玩家所出的牌
        PlayCards playCards = new PlayCards(cards);
        if (playCards.type == null) {
            this.resLogic.sendErrorMsg(playerId, "PlayCards", 1, "");
            log.warn("玩家[{}][{}]出的牌[{}]不符合规则", seat.nickname, seat.playerId, cards);
            return false;
        }

        // 上一次玩家出的牌
        PlayCards prePlayCards = room.prePlayCards;
        // 玩家首个出牌或玩家出牌后别人不要
        if ((prePlayCards == null) || (room.prePlaySeat == room.nextPlaySeat)) {
            this.doPlayCards(seat, playCards);
            return true;
        }

        // 牌的比较结果 (-2:出牌数量或牌型不匹配,-1：我小，0：相等，1：我大)
        int compareRes = CardsTypeComparator.compare(playCards, prePlayCards);
        // 我的牌如果不是王炸或者炸弹那么数量必须和上一家出牌数量一致
        if (compareRes == -2) {
            this.resLogic.sendErrorMsg(playerId, "PlayCards", 2, "");
            log.warn("玩家[{}][{}]出牌[{}]数量或牌型和上家[{}]不匹配", seat.nickname, seat.playerId, playCards.sortedCards, prePlayCards.sortedCards);
            return false;
        } else if ((compareRes == -1) || (compareRes == 0)) {// 我的牌没有上家大
            this.resLogic.sendErrorMsg(playerId, "PlayCards", 3, "");
            log.warn("玩家[{}][{}]出牌[{}]没有上家[{}]大", seat.nickname, seat.playerId, playCards.sortedCards, prePlayCards.sortedCards);
            return false;
        }
        this.doPlayCards(seat, playCards);
        return true;
    }

    /**
     * 玩家出牌(大于上家出牌)
     *
     * @param seat
     * @param playCards
     */
    private void doPlayCards(Seat seat, PlayCards playCards) {
        seat.closeFuture(false);
        Room room = seat.room;
        if (room.nextPlaySeat == room.prePlaySeat) {
            room.playRound++;
        }
        room.prePlaySeat = seat;
        room.prePlayCards = playCards;
        room.nextPlaySeat = room.nextSeat(seat);
        seat.playNum++;
        seat.handCards.cards.removeAll(playCards.sortedCards);
        if ((playCards.type == ZHA_DAN) || (playCards.type == CardsType.WANG_ZHA)) {
            room.bombNum++;
        }
        this.resLogic.sendPlayCardsMsg(room, seat, playCards);

        log.info("玩家[{}][{}]出牌{}", seat.nickname, seat.playerId, playCards.sortedCards);
        // 牌出完
        if (seat.handCards.cards.size() == 0) {
            System.out.println();
            room.closeFuture0();
            this.doGameOver(room, seat);
        } else {
            this.scheduleAutoPlayCards(room.nextPlaySeat);
        }
    }

    /** 放弃出牌 */
    private boolean doGiveUp(Seat seat) {
        Room room = seat.room;
        if (room.prePlaySeat == room.nextPlaySeat) {
            this.resLogic.sendErrorMsg(seat.playerId, "PlayCards", 2, "");
            log.warn("玩家[{}][{}]头家出牌不能放弃", seat.nickname, seat.playerId);
            return false;
        }
        seat.closeFuture(false);
        room.nextPlaySeat = room.nextSeat(seat);
        this.resLogic.sendPlayCardsMsg(room, seat, null);// 发送放弃出牌消息
        log.info("玩家[{}][{}]放弃出牌", seat.nickname, seat.playerId);
        this.scheduleAutoPlayCards(room.nextPlaySeat);
        return true;
    }

    /**
     * 托管
     *
     * @param playerId 玩家id
     */
    public void autoPlay(long playerId) {
        Seat seat = DdzCache.playerSeats.get(playerId);
        if (seat == null) {
            log.warn("座位空[{}]", "autoPlay");
            return;
        }
        this.doAutoPlay(playerId, !seat.auto, true);
    }

    /**
     * 托管
     *
     * @param playerId 玩家id
     * @param auto 状态，true为进入托管，false为取消托管
     */
    private void doAutoPlay(long playerId, boolean auto, boolean autoActive) {
        Seat seat = DdzCache.playerSeats.get(playerId);
        if (seat == null) {
            return;
        }
        seat.auto = auto;
        if (auto == true) {
            seat.autoActive = autoActive;// 主动进入托管
        } else {
            seat.autoActive = false;
        }
        this.doAutoPlayCardAuto(seat, true);
        this.resLogic.sendAutoPlayMsg(seat);
    }

    /**
     * 游戏结束,先结算输家(输家有可能破产、托管赔偿)再结算赢家
     *
     * @param room
     * @param lastPlaySeat 最后出牌玩家
     */
    private void doGameOver(Room room, Seat lastPlaySeat) {
        Seat landlord = room.landlord;
        Seat farmer1 = room.nextSeat(landlord);
        Seat farmer2 = room.preSeat(landlord);

        PlayerSettle landlordSettle = null;
        PlayerSettle f1Settle = null;
        PlayerSettle f2Settle = null;

        if (lastPlaySeat == landlord) {// 地主赢
            room.spring = (farmer1.playNum == 0) && (farmer2.playNum == 0);// 春天
            // begin:计算分数变化，顺序不能变
            f1Settle = this.settle(farmer1, true, new PlayerSettle[] {});
            f2Settle = this.settle(farmer2, true, new PlayerSettle[] {});
            landlordSettle = this.settle(landlord, true, new PlayerSettle[] { f1Settle, f2Settle });
            // end:计算分数变化，顺序不能变
            log.warn("地主[{}][{}]赢金币[{}]", lastPlaySeat.nickname, lastPlaySeat.playerId, landlordSettle.getGold());
        } else {// 地主输
            room.spring = landlord.playNum == 1;// 反春天
            // begin:计算分数变化，顺序不能变
            landlordSettle = this.settle(landlord, false, new PlayerSettle[] {});
            f1Settle = this.settle(farmer1, false, new PlayerSettle[] { landlordSettle });
            f2Settle = this.settle(farmer2, false, new PlayerSettle[] { landlordSettle });
            // end:计算分数变化，顺序不能变
            log.warn("地主[{}][{}]输金币[{}]", landlord.nickname, landlord.playerId, landlordSettle.getGold());
        }

        // 发送游戏结束消息
        List<PlayerSettle> playerSettles = new ArrayList<>();
        playerSettles.add(landlordSettle);
        playerSettles.add(f1Settle);
        playerSettles.add(f2Settle);
        this.resLogic.sendGameOverMsg(room, playerSettles);
        this.saveRecord(room, playerSettles);
        this.syncRecordToLobby(playerSettles);

        log.info("玩家[{}][{}][{}]出完所有牌获胜", lastPlaySeat.nickname, lastPlaySeat.playerId, room.landlord == lastPlaySeat ? "地主" : "农民");

        room.closeFuture(true);
        room.closeRobotFuture();
        room.closeFuture0();
        RoomConfig config = room.config;
        this.redisUtil.decr(RedisRegionConfig.FILE_ROOM_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
        List<Seat> seats = room.seats;
        for (Seat seat : seats) {
            log.warn("游戏结束玩家[{}]退出房间", seat.playerId);
            DdzCache.playerSeats.remove(seat.playerId);
            if (!seat.robot) {
                this.playerService.syncRoom(0, 0, seat.playerId);
                this.redisUtil.decr(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
            } else {
                this.redisUtil.decr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
            }
            long playerId = seat.playerId;
            String error = MessageFormat.format("服务器停止,玩家[{}]不能进入", playerId);
            log.error(error);
            this.resLogic.sendErrorMsg(playerId, "EnterRoom", 99, error);
            seat.clear();
        }
    }

    /**
     * 结算玩家
     *
     * @param seat 座位
     * @param landlordWin 是否地主赢
     * @param otherSettles 地主输且座位为农民时候传地主结算信息，地主赢且座位为地主时传两个农民结算信息，其他情况传null
     * @return 座位的结算信息
     */
    private PlayerSettle settle(Seat seat, boolean landlordWin, PlayerSettle[] otherSettles) {
        Room room = seat.room;
        RoomConfig config = room.config;
        BigDecimal baseScore = config.getBaseScore();// 底分
        BigDecimal enterScore = config.getEnterScore();// 准入分数
//        int maxMultiple = config.getMaxMultiple();// 最大倍数
        BigDecimal pumpRate = config.getPumpRate();// 服务费百分比
        int bombMultiple = room.bombNum;// 炸弹倍数
        int springMultiple = room.spring ? 2 : 1;
        Seat landlord = room.landlord;
        BigDecimal TWO = new BigDecimal(2);

        int tableMultiple = (1 << bombMultiple) * springMultiple;// 桌子倍数
//        if (tableMultiple>maxMultiple) {// 桌子倍数不能高于最大倍数
//            tableMultiple = maxMultiple;
//        }

        BigDecimal planChangeGold = new BigDecimal(landlord.callScore * tableMultiple).multiply(baseScore);// 计划改变的分数
        if (planChangeGold.compareTo(enterScore) > 0) {// 如果赢的分数超过准入分数，则只输赢准入分数
            planChangeGold = enterScore;
        }

        Player player = null;
        if (seat.robot) {
            player = this.playerService.getRobot(seat.playerId);
        } else {
            player = this.playerService.getPlayerPlatform(seat.playerId);
        }

        PlayerSettle settle = new PlayerSettle();
        settle.setPlayerId(player.getId());// 玩家id
        settle.setNickName(player.getNickname());// 昵称
        settle.setBeforeGold(player.getGold());// 游戏前金币

        if (seat == landlord) {// 座位是地主
            settle.setLandlord(true);// 是否是地主
            if (landlordWin) {// 地主赢
                BigDecimal gold = BigDecimal.ZERO;
                gold = gold.add(otherSettles[0].getGold());
                gold = gold.add(otherSettles[1].getGold());
                gold = gold.negate();
                settle.setGold(gold);// 输赢金币(未扣服务费)
            } else {// 地主输
                if (player.getGold().compareTo(planChangeGold.multiply(TWO)) < 0) {
                    settle.setGold(player.getGold().negate());// 输赢金币(未扣服务费)
                } else {
                    settle.setGold(planChangeGold.multiply(TWO).negate());// 输赢金币(未扣服务费)
                }
            }
        } else {// 座位是农民
            settle.setLandlord(false);// 是否是地主
            if (!landlordWin) {// 农民赢
                BigDecimal gold = BigDecimal.ZERO;
                gold = gold.add(otherSettles[0].getGold().divide(TWO));
                gold = gold.negate();
                settle.setGold(gold);// 输赢金币(未扣服务费)
            } else {// 农民输
                if (player.getGold().compareTo(planChangeGold) < 0) {
                    settle.setGold(player.getGold().negate());// 输赢金币(未扣服务费)
                } else {
                    settle.setGold(planChangeGold.negate());// 输赢金币(未扣服务费)
                }
            }
        }

        if (settle.getGold().compareTo(planChangeGold) > 0) {
            BigDecimal winGold = settle.getGold();
            BigDecimal charge = BigDecimal.ZERO;
            charge = winGold.multiply(pumpRate);
            charge = charge.divide(new BigDecimal(100));
            winGold = winGold.subtract(charge);

            settle.setWinGold(winGold);// 输赢金币(已扣服务费)
            settle.setCharge(charge);// 服务费
        } else {
            settle.setWinGold(settle.getGold());// 输赢金币(已扣服务费)
            settle.setCharge(BigDecimal.ZERO);// 服务费
        }

        settle.setAfterGold(player.getGold().add(settle.getWinGold()));// 游戏后金币
        settle.setCards(Card.getOutCard(seat.handCards.getSortCards()));// 玩家剩余牌
        log.info("[{}][{}]结算金币[{}]", seat.nickname, seat.playerId, settle.getWinGold());

        if (!seat.robot) {// 不是机器人则更新玩家金币
            player.setGold(settle.getAfterGold());
            this.playerService.updatePlayer(player);
        } else {// 是机器人则更新库存
            if (settle.getGold().compareTo(BigDecimal.ZERO) > 0) {
                this.redisUtil.incr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + config.getLevel(), settle.getGold().doubleValue());
            } else {
                this.redisUtil.decr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + config.getLevel(), settle.getGold().negate().doubleValue());
            }
        }
        return settle;
    }

    /**
     * 保存战绩
     */
    private void saveRecord(Room room, List<PlayerSettle> settles) {
        List<Map<String, Object>> userResultList = new ArrayList<>();
        for (PlayerSettle settle : settles) {
            Map<String, Object> map = new HashMap<>();
            userResultList.add(map);
            map.put("userId", settle.getPlayerId());// 用户id
            map.put("landlord", settle.isLandlord());// 是否是地主
            map.put("winLosGold", settle.getGold());// 输赢金币(未扣服务费)
        }
        Date gameDate = new Date();
        RoomConfig config = room.config;
        String roundCode = RoundIdUtils.getGameRecordId(String.valueOf(this.gameId), String.valueOf(room.uniquId));
        for (PlayerSettle settle : settles) {
            Seat seat = DdzCache.playerSeats.get(settle.getPlayerId());
            GameRecordDTOBuilder<Map<String, Object>> build = GameRecordDTO.builder();
            build.gameDate(gameDate);// 游戏牌局时间
            build.userId(settle.getPlayerId());// 用户id
            build.userName(settle.getNickName());// 用户昵称
            build.gameId(this.gameId);// 游戏id
            build.fileId(config.getLevel());// 场次id
            build.fileName(config.getName());// 场次名称
            build.baseScore(config.getBaseScore());// 游戏底分
            build.role(settle.isLandlord() ? "地主" : "农民");
            build.callBranch(seat.callScore);// 叫分
            Map<String, Integer> multiples = new HashMap<>();
            int multipleValue = 1;
            if (room.spring) {
                multiples.put("春天", 2);
                multipleValue = multipleValue << 1;
            }
            if (room.bombNum > 0) {
                multiples.put("炸弹", 1 << room.bombNum);
                multipleValue = multipleValue << room.bombNum;
            }
            multiples.put("总倍数", multipleValue);
            build.multiples(JSON.toJSONString(multiples));// 倍数
            build.gameResult(userResultList);// 游戏结果
            build.beforeGameGold(settle.getBeforeGold());// 游戏前金币
            build.afterGameGold(settle.getAfterGold());// 游戏后金币
            if (settle.isLandlord()) {
                build.betsGold(settle.getWinGold().divide(new BigDecimal(2)).setScale(2, BigDecimal.ROUND_HALF_UP).abs());// 下注值
            } else {
                build.betsGold(settle.getWinGold().abs());// 下注值
            }
            build.winLosGold(settle.getWinGold());// 输赢金币
            build.serviceCharge(settle.getCharge());// 服务费
            build.isRobot(seat.robot);// 是否是机器人
            build.roundCode(roundCode);
            try {
                this.defautProducer.send(JSON.toJSONString(build.build()));
            } catch (Exception e) {
                log.error("战绩信息发送异常:" + seat.playerId, e);
            }
        }
    }

    /** 同步金币到大厅 */
    private void syncRecordToLobby(List<PlayerSettle> settles) {
        for (PlayerSettle settle : settles) {
            Seat seat = DdzCache.playerSeats.get(settle.getPlayerId());
            if (seat.robot) {
                continue;
            }
            // 发送修改金币到大厅
            log.info("[{}][{}]结算发送金币[{}]", settle.getNickName(), settle.getPlayerId(), settle.getWinGold());
            try {
                SettleSendDto settleSendDto = SettleSendDto.builder().build();
                settleSendDto.setUserId(settle.getPlayerId());
                settleSendDto.setChangeGold(settle.getWinGold());
                this.playerService.settle(settleSendDto);
            } catch (Exception e) {
                log.error("同步金币结算错误", e);
            }
        }
    }

    /**
     * 退出房间
     */
    public synchronized void exitRoom(long playerId) {
        Seat seat = DdzCache.playerSeats.get(playerId);
        // 有一些情况下会为空
        if (seat == null || seat.room == null) {
            return;
        }
        Room room = seat.room;
        if (room.stage != Stage.WAIT) {
            seat.online = false;
            this.resLogic.sendExitRoomMsgForce(seat);
            log.warn("玩家[{}][{}]不能主动退出房间,当前是[{}]阶段", seat.nickname, seat.playerId, room.stage);
            return;
        }
        this.doExitRoom(seat);
    }

    /**
     * 退房间
     *
     * @param seat
     */
    public synchronized void doExitRoom(Seat seat) {
        if (seat == null) {
            log.warn("座位空[{}]", "doExitRoom");
            return;
        }
        Room room = seat.room;
        RoomConfig config = room.config;

        synchronized (room) {
            if (room.stage != Stage.WAIT) {// 准备阶段可以任意退
                log.info("玩家[{}][{}]非准备阶段退出房间[{}]", seat.playerId, seat.nickname, room.config.name);
                return;
            }
            log.info("玩家[{}][{}]退出房间[{}][{}]", seat.playerId, seat.nickname, room.config.name, room.uniquId);
            this.resLogic.sendExitRoomMsg(seat);
            DdzCache.playerSeats.remove(seat.playerId);
            if (!seat.robot) {
                this.playerService.syncRoom(0, 0, seat.playerId);
                this.redisUtil.decr(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
            } else {
                this.redisUtil.decr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
            }
            seat.clear();
            if (room.isSeatEnpty()) {
                room.closeFuture(true);
                room.closeRobotFuture();
                room.closeFuture0();
                DdzCache.curNotStartRooms.get(config.getLevel()).remove(room);
                this.redisUtil.decr(RedisRegionConfig.FILE_ROOM_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
                for (Seat s : room.seats) {
                    if (s.playerId == 0) {
                        continue;
                    }
                    this.redisUtil.decr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + config.getLevel(), 1.0);
                    DdzCache.playerSeats.remove(s.playerId);
                    s.clear();
                }
            }
        }
    }

    /**
     * 掉线
     */
    public void lostOnline(long playerId) {
        Seat seat = DdzCache.playerSeats.get(playerId);
        if (seat == null) {
            return;
        }
        seat.online = false;
        if (seat.room.stage == Stage.WAIT) {
            this.doExitRoom(seat);
        }
        if (seat.room.stage == Stage.GAME) {
            this.doAutoPlay(playerId, true, seat.autoActive);
        }
    }

    /**
     * 心跳处理
     */
    public void heartbeatLost(long playerId) {
        Seat seat = DdzCache.playerSeats.get(playerId);
        if (seat == null) {
            return;
        }
        seat.heartbeatLost = false;
        this.resLogic.sendHeartbeatLost(seat);
    }

    /**
     * 心跳检测
     */
    public void heartbeatLostTest() {
        this.worker.scheduleWithFixedDelay(() -> {
            try {
                for (Seat seat : DdzCache.playerSeats.values()) {
                    if (seat.playerId == 0) {
                        continue;
                    }
                    if (!seat.online) {
                        continue;
                    }
                    if (seat.heartbeatLost) {
                        SocketMgr socketMgr = SpringUtil.getBean(SocketMgr.class);
                        SocketServer socket = socketMgr.getSocket(seat.playerId);
                        if (socket != null) {
                            socket.closeSession();
                        }
                    } else {
                        seat.heartbeatLost = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 10, 5, TimeUnit.SECONDS);
    }

    /**
     * 停服
     */
    public void stopService() {
        this.STOP_SERVICE = true;
    }
}
