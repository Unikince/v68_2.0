package com.dmg.fish.business.logic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.fish.business.config.dic.FishCtrlReturnRateDic;
import com.dmg.fish.business.config.dic.FishCtrlStockDic;
import com.dmg.fish.business.config.dic.FishDic;
import com.dmg.fish.business.config.dic.FishRoomDic;
import com.dmg.fish.business.config.dic.FishScenceDic;
import com.dmg.fish.business.config.wrapper.FishRoomWrapper;
import com.dmg.fish.business.config.wrapper.FishScenceWrapper;
import com.dmg.fish.business.model.Constant;
import com.dmg.fish.business.model.FishScence;
import com.dmg.fish.business.model.fish.Fish;
import com.dmg.fish.business.model.fish.FishType;
import com.dmg.fish.business.model.room.Room;
import com.dmg.fish.business.model.room.Seat;
import com.dmg.fish.business.model.room.StockValue;
import com.dmg.fish.business.model.room.Table;
import com.dmg.fish.business.platform.model.Player;
import com.dmg.fish.business.platform.service.PlayerService;
import com.dmg.fish.core.work.Worker;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlReturnRateDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishCtrlStockDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishDTO;
import com.dmg.gameconfigserverapi.fish.dto.FishRoomDTO;
import com.dmg.gameconfigserverapi.fish.userCtrl.UserControlFeign;
import com.dmg.gameconfigserverapi.fish.userCtrl.UserControlListDTO;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.model.dto.GameRecordDTO.GameRecordDTOBuilder;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.server.common.util.RoundIdUtils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

/**
 * 实现逻辑
 */
@Service
public class FishMgr {
    private static final Logger log = LoggerFactory.getLogger(FishMgr.class);
    /** 玩家的座位 */
    private final ConcurrentHashMap<Long, Seat> playerSeats = new ConcurrentHashMap<>();
    /** 游戏房间 */
    private final Map<Integer, Room> rooms = new ConcurrentHashMap<>();
    @Value("${game-id}")
    private int gameId;
    @Autowired
    private Worker worker;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private FishMsgMgr fishMsgMgr;
    @Autowired
    private FishStrategyMgr fishStrategyMgr;
    @Autowired
    private FishScenceDic fishScenceDic;
    @Autowired
    private FishDic fishDic;
    @Autowired
    private FishRoomDic fishRoomDic;
    @Autowired
    private FishCtrlStockDic fishCtrlStockDic;
    @Autowired
    private FishCtrlReturnRateDic fishCtrlReturnRateDic;
    @Autowired
    private UserControlFeign userControlFeign;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private MQProducer defautProducer;
    /** 停服标注 */
    private boolean STOP_SERVICE = false;
    /** 用于下面定时器做间隔 */
    private long scheduleTime;

    @PostConstruct
    private void init() {
        this.worker.schedule(() -> {
            this.initRooms();// 初始化房间
            this.initStock();// 初始化库存
            this.initRoomCtrlModel();// 初始化房间控制模型
            this.worker.scheduleWithFixedDelay(() -> {
                try {
                    if (this.scheduleTime == 0) {
                        this.initRooms();// 初始化房间
                        this.initStock();// 初始化库存
                        this.initRoomCtrlModel();// 初始化房间控制模型
                    }
                    this.syncFunction();
                } catch (Exception e) {
                    log.error("定时器错误", e);
                }
                this.scheduleTime++;
            }, 0, 1000, TimeUnit.MILLISECONDS);
        }, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * 初始化房间
     */
    private void initRooms() {
        for (FishRoomWrapper config : this.fishRoomDic.values()) {
            int roomId = config.getId();
            this.redisUtil.set(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + roomId, "0");
            this.redisUtil.set(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + roomId, "0");
            GameOnlineChangeUtils.initOnlineNum(this.gameId, roomId);
            Room room = new Room(roomId, config.getName(), config.getTableNum(), config.getChair(), config.getScencesData());
            this.rooms.put(roomId, room);
            FishMgr.log.info("初始化房间[{}][{}]", config.getId(), config.getName());
        }
    }

    /**
     * 初始化库存
     */
    private void initStock() {
        for (FishRoomWrapper config : this.fishRoomDic.values()) {
            // 库存数量
            String stock = (String) this.redisUtil.get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + config.getId());
            if (StringUtils.isBlank(stock)) {
                this.redisUtil.incr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + config.getId(), 1.0);
            }
            // 下注数量
            String bet = (String) this.redisUtil.get(RedisRegionConfig.FILE_BET_GOLD_KEY + ":" + this.gameId + "_" + config.getId());
            if (StringUtils.isBlank(bet)) {
                this.redisUtil.incr(RedisRegionConfig.FILE_BET_GOLD_KEY + ":" + this.gameId + "_" + config.getId(), 1.0);
            }
            // 赔付数量
            String payout = (String) this.redisUtil.get(RedisRegionConfig.FILE_PAYOUT_GOLD_KEY + ":" + this.gameId + "_" + config.getId());
            if (StringUtils.isBlank(payout)) {
                this.redisUtil.incr(RedisRegionConfig.FILE_PAYOUT_GOLD_KEY + ":" + this.gameId + "_" + config.getId(), 1.0);
            }
        }
    }

    /**
     * 初始化房间控制模型
     */
    private void initRoomCtrlModel() {
        for (FishRoomWrapper config : this.fishRoomDic.values()) {
            Room room = this.rooms.get(config.getId());
            StockValue stock = room.getReadStock();
            Long returnRate = stock.payout * 10000 / stock.bet;// 返奖率
            room.ctrlModel = this.fishCtrlReturnRateDic.getByReturnRate(returnRate);
        }
    }

    /**
     * 同步方法
     */
    private void syncFunction() {
        if (this.scheduleTime % 5 == 0) {// 每1秒
            this.syncStock();// 库存同步
            this.syncRoomCtrlModel();// 房间控制模型同步
        }

        if (this.scheduleTime % 10 == 0) {// 每10秒
            this.clearPlayers();// 检测是否有玩家需要踢出房间
        }

        if (this.scheduleTime % 15 == 0) {// 每15秒
            for (Seat seat : this.playerSeats.values()) {
                this.settleRecordCtrl(seat);// 结算、战绩纪录、控制模型
            }
        }

        if (this.scheduleTime % 20 == 0) {// 每20秒
            this.removeOvertimeBullet();// 删除过期子弹
        }
    }

    /**
     * 库存同步
     */
    private void syncStock() {
        Map<String, String> ctrlStockChangeMap = new HashMap<>();// 控制库存修改值
        for (FishRoomWrapper config : this.fishRoomDic.values()) {
            Room room = this.rooms.get(config.getId());
            StockValue stockValue = room.getChangeStock();
            // 库存数量
            long stock = stockValue.bet - stockValue.payout;
            if (stock > 0) {
                this.redisUtil.incr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + config.getId(), stock + 0.0);
            } else if (stock < 0) {
                this.redisUtil.decr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + this.gameId + "_" + config.getId(), -stock + 0.0);
            }
            if (stock != 0 && this.fishCtrlStockDic.get(config.getId()).isStatus()) {
                ctrlStockChangeMap.put("" + config.getId(), "" + stock);
            }
            if (stockValue.bet > 0) {
                this.redisUtil.incr(RedisRegionConfig.FILE_BET_GOLD_KEY + ":" + this.gameId + "_" + config.getId(), stockValue.bet + 0.0);
            }
            if (stockValue.payout > 0) {
                this.redisUtil.incr(RedisRegionConfig.FILE_PAYOUT_GOLD_KEY + ":" + this.gameId + "_" + config.getId(), stockValue.payout + 0.0);
            }

            // 下注数量
            String betStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_BET_GOLD_KEY + ":" + this.gameId + "_" + config.getId());
            long betValue = Long.parseLong(betStr);
            // 赔付数量
            String payoutStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_PAYOUT_GOLD_KEY + ":" + this.gameId + "_" + config.getId());
            long payoutValue = Long.parseLong(payoutStr);

            room.setReadStock(betValue, payoutValue);
        }
        if (!ctrlStockChangeMap.isEmpty()) {
            this.fishCtrlStockDic.update(ctrlStockChangeMap);
        }
    }

    /**
     * 房间控制模型同步
     */
    private void syncRoomCtrlModel() {
        for (FishRoomWrapper config : this.fishRoomDic.values()) {
            Room room = this.rooms.get(config.getId());
            StockValue stock = room.getReadStock();
            Long returnRate = stock.payout * 10000 / stock.bet;// 返奖率
            FishCtrlReturnRateDTO dto = this.fishCtrlReturnRateDic.getByReturnRate(returnRate);
            int curCtrlModel = room.ctrlModel.getId();
            if (curCtrlModel != 4) {
                if (dto.getId() == 4) {
                    if (curCtrlModel < 4) {
                        if (returnRate <= 9000) {
                            room.ctrlModel = dto;
                        }
                    } else {
                        if (returnRate >= 9000) {
                            room.ctrlModel = dto;
                        }
                    }
                }
            } else {
                room.ctrlModel = dto;
            }
        }
    }

    /**
     * 结算、战绩纪录、控制模型
     */
    private void settleRecordCtrl(Seat seat) {
        if (seat.robot) {
            return;
        }

        // begin:计算金币
        long preGold = 0;
        long preTotalFireScore = 0;
        long preTotalFishScore = 0;

        long gold = 0;
        long totalFireScore = 0;
        long totalFishScore = 0;
        synchronized (seat) {
            preGold = seat.preGold;
            preTotalFireScore = seat.preTotalFireScore;
            preTotalFishScore = seat.preTotalFishScore;

            seat.preGold = seat.gold;
            seat.preTotalFireScore = seat.totalFireScore;
            seat.preTotalFishScore = seat.totalFishScore;

            gold = seat.preGold;
            totalFireScore = seat.preTotalFireScore;
            totalFishScore = seat.preTotalFishScore;
        }
        long bet = totalFireScore - preTotalFireScore;
        long payout = totalFishScore - preTotalFishScore;

        long winGold = payout - bet;// 时间段输赢的钱
        long winGold2 = gold - preGold;// 时间段输赢的钱2
        if (winGold != winGold2) {
            log.error("两种计算方式输赢金币有差异[{}][{}]", winGold, winGold2);
            return;
        }

        // end:计算金币

        this.syncPlayerCtrlModel(seat);
        this.doSaveRecord(seat, preGold, gold, bet, payout, winGold);// 同步战绩
        this.doSettle(seat, winGold);// 同步结算金币

    }

    /**
     * 同步玩家模型
     */
    private void syncPlayerCtrlModel(Seat seat) {
        seat.ctrlType = 0;
        seat.ctrlModel = null;
        if (seat.ctrlModel == null) {
            this.syncPlayerPointCtrlModel(seat);
        }
        if (seat.ctrlModel == null) {
            this.syncAutoPointCtrlModel(seat);
        }
        if (seat.ctrlModel == null) {
            this.syncStockCtriModel(seat);
        }
    }

    /**
     * 同步点控模型
     */
    private void syncPlayerPointCtrlModel(Seat seat) {
        int model = this.userControlFeign.getPointControlModel(seat.playerId);
        if (model > 0) {
            seat.ctrlType = 1;
            seat.ctrlModel = this.fishCtrlReturnRateDic.get(model);
        }
    }

    /**
     * 同步自控模型
     */
    private void syncAutoPointCtrlModel(Seat seat) {
        int model = this.userControlFeign.getAutoControlModel(seat.playerId);
        if (model > 0) {
            seat.ctrlType = 2;
            seat.ctrlModel = this.fishCtrlReturnRateDic.get(model);
        }
    }

    /**
     * 同步库存模型
     */
    private void syncStockCtriModel(Seat seat) {
        FishCtrlStockDTO ctrlDto = this.fishCtrlStockDic.get(seat.table.room.id);
        if (ctrlDto == null) {
            return;
        }
        if (!ctrlDto.isStatus()) {
            return;
        }
        UserControlListDTO userDTO = this.userControlFeign.getUserControlInfo(seat.playerId);
        if (userDTO == null) {
            return;
        }
        long playerRunningWater = (long) (userDTO.getTotalBet().doubleValue() * 100);
        long needRunningWater = ctrlDto.getRunningWater();

        boolean flag = false;
        switch (ctrlDto.getType()) {
            case 1: {// >=
                if (playerRunningWater >= needRunningWater) {
                    flag = true;
                }
                break;
            }
            case 2: {// >
                if (playerRunningWater > needRunningWater) {
                    flag = true;
                }
                break;
            }
            case 3: {// <=
                if (playerRunningWater <= needRunningWater) {
                    flag = true;
                }
                break;
            }
            case 4: {// <
                if (playerRunningWater < needRunningWater) {
                    flag = true;
                }
                break;
            }
            default: {
                break;
            }
        }
        if (flag) {
            seat.ctrlType = 3;
            seat.ctrlModel = this.fishCtrlReturnRateDic.get(ctrlDto.getModel());
        }

    }

    /**
     * 保存战绩
     */
    private void doSaveRecord(Seat seat, long preGold, long gold, long bet, long payout, long winGold) {
        if (bet == 0 && payout == 0) {
            return;
        }
        Table table = seat.table;
        Room room = table.room;
        FishRoomWrapper config = this.fishRoomDic.get(room.id);
        BigDecimal beforeGameGold = new BigDecimal(preGold).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal afterGameGold = new BigDecimal(gold).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal winLosGold = new BigDecimal(winGold).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);

        BigDecimal betsGold = new BigDecimal(bet).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);

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
        build.serviceCharge(BigDecimal.ZERO);// 服务费
        build.roundCode(RoundIdUtils.getGameRecordId(String.valueOf(this.gameId), String.valueOf(seat.playerId)));
        build.isRobot(seat.robot);// 是否是机器人
        if (seat.ctrlType == 1) {
            build.controlState(true);
        }
        try {
            this.defautProducer.send(JSON.toJSONString(build.build()));
        } catch (Exception e) {
            log.error("战绩信息发送异常:" + seat.playerId, e);
        }
    }

    /**
     * 发送结算金币
     */
    private void doSettle(Seat seat, long winGold) {
        if (winGold == 0) {
            return;
        }
        BigDecimal sendGold = new BigDecimal(winGold).divide(new BigDecimal(100.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.playerService.settle(seat.playerId, sendGold);
    }

    /**
     * 检测是否有玩家需要踢出房间
     */
    public void clearPlayers() {
        long now = System.currentTimeMillis();

        for (Entry<Long, Seat> entry : this.playerSeats.entrySet()) {
            Seat seat = entry.getValue();
            FishRoomDTO roomConfig = this.fishRoomDic.get(seat.table.room.id);
            long playerId = seat.playerId;
            if (playerId == 0) {
                seat.clear();
                this.playerSeats.remove(entry.getKey());
                continue;
            }
            if (this.STOP_SERVICE) {
                if (!seat.robot) {
                    this.doExitRoom(seat);
                }
            } else {
                if ((now - seat.preFireTime) > (roomConfig.getKickTime() * 1000L)) {
                    FishMgr.log.info("玩家[{}][{}]超时未操作T除房间", playerId, seat.nickName);
                    this.doExitRoom(seat);
                    this.fishMsgMgr.sendTimeoutExitMsg(playerId);
                }
            }
        }
    }

    /**
     * 删除过期子弹
     */
    private void removeOvertimeBullet() {
        long time = DateUtil.offsetSecond(new Date(), -20).getTime();
        for (Seat seat : this.playerSeats.values()) {
            if (!seat.robot) {
                continue;
            }
            for (Entry<Integer, Long> entry : seat.bulletTimes.entrySet()) {
                if (time < entry.getValue()) {
                    continue;
                }
                seat.bullets.remove(entry.getKey());
                seat.bulletTimes.remove(entry.getKey());
                seat.table.decrementBullet();
            }
        }
    }

    /**
     * 检查是否可以进入房间
     */
    public void checkRoom(Long playerId, int roomId) {
        Player player = this.playerService.getPlayerPlatform(playerId);
        FishRoomDTO roomConfig = this.fishRoomDic.get(roomId);

        if (roomConfig == null) {
            log.error("玩家[{}]检查是否可以进入[{}]房间时配置为空", playerId, roomId);
            return;
        }

        if (!roomConfig.isOpen()) {
            log.warn("玩家[{}]进入房间[{}]未开放", playerId, roomId);
            this.fishMsgMgr.sendCheckRoomMsg(playerId, false);
            return;
        }

        int playerNum = 0;
        String playerNumStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + roomConfig.getId());
        if (StringUtils.isNotBlank(playerNumStr)) {
            playerNum = Integer.parseInt(playerNumStr);
        }
        if (playerNum > roomConfig.getPlayerUpLimit()) {
            log.warn("玩家[{}]进入房间[{}]已达人数上限[{}]", playerId, roomId, roomConfig.getPlayerUpLimit());
            this.fishMsgMgr.sendCheckRoomMsg(playerId, false);
            return;
        }

        long gold = player.getGold();
        if (gold < roomConfig.getGoldLimitLower() || gold > roomConfig.getGoldLimitUpper()) {
            log.warn("玩家[{}]检查进入房间[{}]金币不满足,当前金币[{}],所需金币[{}]~[{}]", playerId, roomId, gold, roomConfig.getGoldLimitLower(), roomConfig.getGoldLimitUpper());
            this.fishMsgMgr.sendCheckRoomMsg(playerId, false);
            return;
        }
        int robotTableNum = this.fishRoomDic.get(roomId).getRobotTableNum();
        Room room = this.rooms.get(roomId);
        Seat emptySeat = player.isRobot() ? room.findEmptySeatForRobot(null, robotTableNum) : room.findEmptySeatForPlayer(null);
        if (emptySeat == null) {
            log.warn("玩家[{}]检查进入房间[{}]座位空", playerId, roomId, gold);
            this.fishMsgMgr.sendCheckRoomMsg(playerId, false);
            return;
        }

        this.fishMsgMgr.sendCheckRoomMsg(playerId, true);
    }

    /**
     * 玩家进入房间,自动分桌
     *
     * @param playerId
     * @param roomId
     */
    public void enterRoom(Long playerId, int roomId, boolean robot) {
        Player player = null;
        FishRoomWrapper roomConfig = this.fishRoomDic.get(roomId);
        if (robot) {
            player = this.playerService.getRobot(playerId, roomConfig.getRobotEnterPointLower(), roomConfig.getRobotEnterPointUpper());
        } else {
            player = this.playerService.getPlayerPlatform(playerId);
        }
        if (player == null) {
            log.error("玩家[{}]不存在", playerId);
            return;
        }
        if (player.getId() == 0) {
            log.error("玩家id为0");
            return;
        }

        Seat seat = this.playerSeats.get(player.getId());
        if (seat != null) {
            if (seat.playerId == 0) {
                seat.clear();
                this.playerSeats.remove(player.getId());
            } else {
                this.fishMsgMgr.sendEnterRoomMsg(seat, com.dmg.common.pb.java.Fish.FishCode.SUCCESS);
                FishMgr.log.info("玩家[{}][{}]重新进入房间", seat.nickName, seat.playerId);
                return;
            }
        }

        Room room = this.rooms.get(roomId);
        if (room == null) {
            FishMgr.log.warn("玩家[{}][{}]进入的房间[{}]不存在", player.getNickname(), player.getId(), roomId);
            return;
        }

        if (!roomConfig.isOpen()) {
            log.warn("玩家[{}]进入房间[{}]未开放", playerId, roomId);
            return;
        }

        // 桌子机器人数
        int robotTableNum = this.fishRoomDic.get(roomId).getRobotTableNum();
        Seat emptySeat = player.isRobot() ? room.findEmptySeatForRobot(null, robotTableNum) : room.findEmptySeatForPlayer(null);
        if (emptySeat == null) {
            FishMgr.log.warn("玩家[{}][{}]进入的房间[{}]人数已满", player.getNickname(), player.getId(), roomId);
            this.fishMsgMgr.sendEnterRoomMsg(roomId, playerId);
            return;
        }

        this.doEnterRoom(player, emptySeat);
    }

    /**
     * 玩家进入房间的位置
     *
     * @param player
     * @param seat
     */
    private void doEnterRoom(Player player, Seat seat) {
        if (this.STOP_SERVICE) {
            return;
        }
        if (player.getId() == 0) {
            return;
        }
        Table table = seat.table;
        Room room = table.room;
        FishRoomWrapper roomConfig = this.fishRoomDic.get(room.id);

        if (!seat.robot) {
            long playerNum = 0;
            String playerNumStr = (String) this.redisUtil.get(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + roomConfig.getId());
            if (StringUtils.isNotBlank(playerNumStr)) {
                playerNum = Long.parseLong(playerNumStr);
            }
            if (playerNum > roomConfig.getPlayerUpLimit()) {
                log.warn("玩家[{}]进入房间[{}]已达人数上限[{}]", player.getId(), room.id, roomConfig.getPlayerUpLimit());
                return;
            }
        }

        if (table.playersNum() == 0) {
            for (Seat s : table.seats) {
                if ((s.playerId > 0) && s.robot) {
                    s.bullets.clear();
                    s.bulletTimes.clear();
                }
            }
            table.bulletCount = 0;
        }

        // 玩家捕鱼模块数据
        long playerId = player.getId();

        seat.playerId = playerId;
        seat.nickName = player.getNickname();
        seat.icon = player.getHeadImg();
        seat.sex = player.getSex();
        seat.gold = player.getGold();
        seat.preGold = seat.gold;
        seat.robot = player.isRobot();
        seat.preFireTime = System.currentTimeMillis();
        seat.batteryScore = this.fishRoomDic.get(room.id).getBatteryScoresTree().first();

        this.playerSeats.put(playerId, seat);

        this.settleRecordCtrl(seat);

        if (!player.isRobot()) {
            this.playerService.syncRoom(room.id, room.id, playerId);
            this.redisUtil.incr(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + room.id, 1.0);
            GameOnlineChangeUtils.incOnlineNum(this.gameId, room.id);
        } else {
            this.redisUtil.incr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + room.id, 1.0);
        }

        FishMgr.log.info("玩家[{}][{}]进入房间[{}]桌子[{}]座位[{}]成功", player.getNickname(), playerId, room.id, table.id, seat.order);
        if (table.playersNum() == 1) {// 第一个玩家进入桌子开始刷场景
            this.startScence(table, 0);
        }

        this.fishMsgMgr.sendEnterRoomMsg(seat, com.dmg.common.pb.java.Fish.FishCode.SUCCESS);
        this.fishMsgMgr.sendOtherEnterTableMsg(seat);

        /**
         * 机器人代发碰撞玩家处理
         */
        if (seat.robot) {
            if (table.leader != null) {
                this.fishMsgMgr.sendInsteadPlayersMsg(table);
            }
        } else {
            if (table.leader == null) {
                table.leader = seat;
                this.fishMsgMgr.sendInsteadPlayersMsg(table);
            }
        }
    }

    /**
     * 开始场景
     *
     * @param table
     * @param scenceIndex 新场景Index
     */
    private void startScence(Table table, int scenceIndex) {
        Room room = table.room;
        // 清空当前场景
        table.scence().reset();

        table.scenceIndex = scenceIndex;
        FishScence scence = table.scence();
        FishScenceWrapper scenceConfig = this.fishScenceDic.get(scence.id);
        // 策略刷鱼,刷鱼时间要加上切换场景的时间
        this.fishStrategyMgr.produceFishs(scence, System.currentTimeMillis() + Constant.SWITCH_SCENCE_DELAY);

        // schedule下一个场景
        table.switchScenceFuture = this.worker.schedule(() -> {
            this.startScence(table, (scenceIndex + 1) % table.scences.size());
            this.fishMsgMgr.sendScenceMsg(table);
        }, scenceConfig.getTime(), TimeUnit.SECONDS);

        FishMgr.log.info("房间[{}][{}]桌子[{}]开始场景[{}]", room.id, room.name, table.id, scenceIndex);
    }

    /**
     * 开火
     *
     * @param playerId
     * @param angle
     */
    public void fire(Long playerId, int angle, int bulletId, int fishId) {
        Seat seat = this.playerSeats.get(playerId);
        if (seat == null) {
            return;
        }
        long now = System.currentTimeMillis();
        // 如果房间里没有真实玩家， robot fire无效
        if (seat.table.realPlayersNum() <= 0) {
            return;
        }

        if (seat.table.bulletCount >= this.fishRoomDic.get(seat.table.room.id).getBulletsNumber()) {
            FishMgr.log.warn("玩家[{}][{}]因桌子[{}]屏幕子弹太多， 不能发子弹", seat.nickName, playerId, seat.table.id);
            return;
        }

        if ((now - seat.preFireTime) < Constant.FIRE_CD) {// 两次发炮间隔太小
            FishMgr.log.warn("玩家[{}][{}]发子弹过于频繁", seat.nickName, playerId);
            return;
        }

        if (seat.bullets.containsKey(bulletId)) {// 没有该子弹
            FishMgr.log.warn("玩家[{}][{}]子弹id[{}]重复", seat.nickName, playerId, bulletId);
            return;
        }

        int batteryScore = seat.batteryScore;
        if (seat.gold < batteryScore) {// 玩家分数不够发炮
            FishMgr.log.warn("玩家[{}][{}]金币[{}]不够发炮", seat.nickName, playerId, seat.gold);
            return;
        }
        seat.preFireTime = now;
        seat.gold -= batteryScore;

        seat.totalFireScore += batteryScore;
        seat.bullets.put(bulletId, batteryScore);
        seat.bulletTimes.put(bulletId, System.currentTimeMillis());

        // 子弹加一
        seat.table.incrementBullet();
        this.fishMsgMgr.sendFireMsg(seat, bulletId, angle, fishId);
        this.fishMsgMgr.sendGoldChangeMsg(seat);
    }

    /**
     * 子弹打中鱼,某个客户端会代发机器人打中
     *
     * @param playerId
     * @param bulletId
     * @param fishId 鱼的唯一标识
     */
    public void hit(Long playerId, int bulletId, int fishId) {
        Seat seat = this.playerSeats.get(playerId);
        if (seat == null) {
            FishMgr.log.warn("玩家[{}]不在游戏中不能打中鱼", playerId);
            return;
        }

        if (seat.table.bulletCount <= 0) {
            FishMgr.log.warn("玩家[{}]屏幕中没有子弹， 不能打中鱼", playerId);
            return;
        }

        seat.table.decrementBullet();

        Table table = seat.table;
        Room room = table.room;
        FishScence scence = table.scence();
        // 打中鱼的子弹分数
        Integer bulletScore = seat.bullets.remove(bulletId);
        seat.bulletTimes.remove(bulletId);
        // 被打中的鱼
        Fish fish = scence.fishs.get(fishId);
        if (fish == null) {// 切换场景时产生空鱼
            FishMgr.log.warn("玩家[{}][{}]子弹[{}]打中的鱼不存在", seat.nickName, playerId, bulletId);
            return;
        }
        if (fish.position() == null) {// 未出场鱼
            FishMgr.log.warn("玩家[{}][{}]子弹[{}]打中的鱼未出场", seat.nickName, playerId, bulletId);
            return;
        }
        FishDTO fishConfig = this.fishDic.get(fish.fishId);

        if (bulletScore == null) {// 子弹不存在(客户端先发再通知服务器,切换场景还有时差,也会产生空子弹)
            FishMgr.log.warn("玩家[{}][{}]子弹[{}]不存在", seat.nickName, playerId, bulletId);
            return;
        } else if ((fish == null) || fish.died) {// 鱼不存在或鱼已经死了(两颗子弹同时打中一条鱼,第一个打死,第二个需要还给玩家)
            // 返还子弹
            seat.totalFireScore -= bulletScore;
            seat.gold += bulletScore;
            FishMgr.log.info("玩家[{}][{}]子弹[{}]打中的鱼[{}]不存在或鱼已经死亡反还积分", seat.nickName, playerId, bulletId, fishId);
            return;
        }

        if (!seat.robot) {// 玩家打中鱼，子弹下注值增加
            room.bet(bulletScore.longValue());
        }

        long stockDenominator = 0;
        if (seat.ctrlModel != null) {
            stockDenominator = seat.ctrlModel.getDenominator();
        } else {
            stockDenominator = room.ctrlModel.getDenominator();
        }
        long numerator = Constant.FISH_BASE_DIE_NUMERATOR;// 概率分子
        long denominator = Constant.FISH_FULL_DIE_DENOMINATOR + stockDenominator;// 概率分母
        Set<Fish> dieFishs = new HashSet<>();// 死亡的鱼
        dieFishs.add(fish);
        long totalFishScore = 0;

        switch (FishType.get(fishConfig.getType())) {
            case MULTIPLE: {/* 普通倍数鱼 */
                denominator = denominator * fish.multiple;
                totalFishScore = fish.multiple * bulletScore.longValue();
                break;
            }
            case KINDS_BOMB: { /* 同类炸弹鱼 */
                int subFishMultiple = fish.multiple;
                for (Fish f : scence.fishs.values()) {
                    if (f.id == fishId) {// 当前炸弹鱼
                        continue;
                    }
                    if (f.died) {// 死鱼
                        continue;
                    }
                    if (f.liveTime() <= 0) {// 已过期鱼
                        continue;
                    }
                    if (f.position() == null) {// 未出场鱼
                        continue;
                    }
                    if (f.fishId != 1) {// 当前只配置了鱼id为1的鱼王
                        continue;
                    }
                    dieFishs.add(f);
                    subFishMultiple += f.multiple;
                }
                denominator = denominator * subFishMultiple;
                totalFishScore = subFishMultiple * bulletScore.longValue();
                break;
            }
            default: {
                break;
            }
        }
        boolean die = RandomUtil.randomLong(denominator) < numerator;// 控制鱼是否死亡
        if (die) {
            if (totalFishScore > this.fishRoomDic.get(room.id).getBigPayoutValue()) { // 大额赔付
                StockValue stock = room.getReadStock();
                Long returnRate = stock.payout * 10000 / stock.bet;// 返奖率
                returnRate = (stock.payout + totalFishScore) * 10000 / stock.bet;// 返奖率
                if (returnRate > this.fishRoomDic.get(room.id).getBigPayoutRadio()) {// 返奖率大于90%，拒绝大额赔付
                    return;
                }
            }

            // 被打死的鱼分数(key：鱼id,val:鱼的分数)
            Map<Integer, Long> fishScores = new LinkedHashMap<>();
            for (Fish dieFish : dieFishs) {
                dieFish.died = true;
                int multiple = dieFish.multiple;
                long fishScore = bulletScore.longValue() * multiple;
                fishScores.put(dieFish.id, fishScore);
            }

            seat.totalFishScore += totalFishScore;
            seat.gold += totalFishScore;
            // 赢取的钱从库存扣除
            if (!seat.robot) {// 玩家打死鱼，赔付值增加
                room.payout(totalFishScore);
            }
            this.fishMsgMgr.sendDieMsg(seat, fishScores);
            this.fishMsgMgr.sendGoldChangeMsg(seat);
        }
    }

    /**
     * 增加炮台分数
     *
     * @param playerId
     */
    public void plusBatteryScore(Long playerId) {
        Seat seat = this.playerSeats.get(playerId);
        if (seat == null) {
            FishMgr.log.info("[{}]座位空[{}]", playerId, "minusBatteryScore");
            return;
        }
        // 房间炮台分数配置
        TreeSet<Integer> batteryScores = this.fishRoomDic.get(seat.table.room.id).getBatteryScoresTree();
        // 下一个炮台分数
        Integer nextBatteryScore = batteryScores.higher(seat.batteryScore);
        if (nextBatteryScore != null) {
            this.switchBatteryScore(seat, nextBatteryScore);
        } else {
            this.switchBatteryScore(seat, batteryScores.first());
        }
    }

    /**
     * 减少炮台分数
     *
     * @param playerId
     */
    public void minusBatteryScore(Long playerId) {
        Seat seat = this.playerSeats.get(playerId);
        if (seat == null) {
            FishMgr.log.info("[{}]座位空[{}]", playerId, "minusBatteryScore");
            return;
        }
        // 房间炮台分数配置
        TreeSet<Integer> batteryScores = this.fishRoomDic.get(seat.table.room.id).getBatteryScoresTree();
        // 上一个炮台分数
        Integer preBatteryScore = batteryScores.lower(seat.batteryScore);
        if (preBatteryScore != null) {
            this.switchBatteryScore(seat, preBatteryScore);
        } else {
            this.switchBatteryScore(seat, batteryScores.last());
        }
    }

    /**
     * 切换炮台分数
     *
     * @param seat
     * @param score
     */
    private void switchBatteryScore(Seat seat, int score) {
        seat.batteryScore = score;
        this.fishMsgMgr.sendBatteryChangeMsg(seat);
    }

    /**
     * 锁定鱼
     *
     * @param playerId
     * @param fishId
     */
    public void lock(Long playerId, int fishId) {
        Seat seat = this.playerSeats.get(playerId);
        this.fishMsgMgr.sendLockMsg(seat, fishId);
    }

    /**
     * 取消锁定
     *
     * @param playerId
     */
    public void cancelLock(Long playerId) {
        Seat seat = this.playerSeats.get(playerId);
        this.fishMsgMgr.sendCancelLockMsg(seat);
    }

    /**
     * 掉线
     */
    public void lostOnline(Long playerId) {
        Seat seat = FishMgr.this.getPlayerSeat(playerId);
        if (seat != null) {
            this.doExitRoom(seat);
        }
    }

    /**
     * 金币充值
     */
    public void goldPay(Long playerId, long payGold) {
        Seat seat = this.getPlayerSeat(playerId);
        if (seat != null) {
            seat.gold += payGold;
            seat.preGold += payGold;
            this.fishMsgMgr.sendGoldPayMsg(seat, payGold);
        } else {
            Player player = this.playerService.getPlayer(playerId);
            if (player != null) {
                player.setGold(player.getGold() + payGold);
                this.playerService.update(player);
                this.fishMsgMgr.sendGoldPayMsg(playerId, player.getGold(), payGold);
            }
        }
    }

    /**
     * 玩家退出房间
     *
     * @param plyaerId
     */
    public void exitRoom(Long plyaerId) {
        Seat seat = this.playerSeats.get(plyaerId);
        this.doExitRoom(seat);
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
        long playerId = seat.playerId;
        Table table = seat.table;
        Room room = table.room;

        this.fishMsgMgr.sendExitRoomMsg(seat);

        // 返还子弹的金币
        long returnBulletScore = 0;
        for (Integer bulletScore : seat.bullets.values()) {
            returnBulletScore += bulletScore;
            seat.table.decrementBullet();
        }

        if (returnBulletScore > 0) {
            seat.totalFireScore -= returnBulletScore;
            seat.gold += returnBulletScore;
        }

        this.settleRecordCtrl(seat);

        if (!seat.robot) {
            this.playerService.syncRoom(0, 0, seat.playerId);
            this.redisUtil.decr(RedisRegionConfig.FILE_PLAYER_NUM_KEY + ":" + this.gameId + "_" + room.id, 1.0);
            GameOnlineChangeUtils.decOnlineNum(this.gameId, room.id);
        } else {
            this.redisUtil.decr(RedisRegionConfig.FILE_ROBOT_NUM_KEY + ":" + this.gameId + "_" + room.id, 1.0);
        }

        // 玩家捕鱼游戏日志
        FishMgr.log.info("玩家[{}][{}]退出房间[{}]桌子[{}]", seat.nickName, playerId, room.name, table.id);
        this.playerSeats.remove(playerId);
        seat.clear();

        if (table.leader == seat) {// 桌子leader退出
            table.leader = null;
            for (Seat s : table.seats) {
                if (!s.robot && (s.playerId > 0)) {// 找下一个leader代发碰撞
                    table.leader = s;
                    this.fishMsgMgr.sendInsteadPlayersMsg(table);
                    break;
                }
            }
        }

        if (table.leader == null) {
            for (Seat s : table.seats) {
                if ((s.playerId > 0) && s.robot) {
                    s.bullets.clear();
                    s.bulletTimes.clear();
                }
            }
            table.bulletCount = 0;
        }

        if (table.playersNum() == 0) {
            table.scence().reset();
            table.reset();
        }
    }

    public Seat getPlayerSeat(Long playerId) {
        return this.playerSeats.get(playerId);
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

    public ConcurrentHashMap<Long, Seat> getPlayerSeats() {
        return this.playerSeats;
    }

}
