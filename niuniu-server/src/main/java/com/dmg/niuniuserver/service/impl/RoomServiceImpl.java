package com.dmg.niuniuserver.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import com.dmg.niuniuserver.service.config.UserControlService;
import com.dmg.server.common.enums.GameOddsEnum;
import com.dmg.server.common.enums.UserControlStateEnum;
import com.dmg.server.common.model.dto.GameWaterPoolDTO;
import com.dmg.server.common.model.dto.UserControlInfoDTO;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.server.common.util.RoundIdUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.constant.Constant;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.manager.TimerManager;
import com.dmg.niuniuserver.manager.work.BalanceResultDelayWork;
import com.dmg.niuniuserver.manager.work.NotifyPlayerNoteCountdownTask;
import com.dmg.niuniuserver.manager.work.ReadyPlayerDelayWork;
import com.dmg.niuniuserver.manager.work.ResultAllWork;
import com.dmg.niuniuserver.manager.work.ToRobBankerWork;
import com.dmg.niuniuserver.model.bean.Balance;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.model.bean.Poker;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.ActionType;
import com.dmg.niuniuserver.model.constants.Combination;
import com.dmg.niuniuserver.model.constants.GameConfig;
import com.dmg.niuniuserver.model.constants.LeaveReason;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.model.constants.SeatState;
import com.dmg.niuniuserver.model.dto.BankerInfoDTO;
import com.dmg.niuniuserver.model.dto.BrandDTO;
import com.dmg.niuniuserver.model.dto.RecordDataDTO;
import com.dmg.niuniuserver.model.dto.RoomInfoDTO;
import com.dmg.niuniuserver.model.dto.SeatMsgDTO;
import com.dmg.niuniuserver.model.dto.SendCardsMsgDTO;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.dmg.niuniuserver.service.ALGService;
import com.dmg.niuniuserver.service.IdGeneratorService;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;
import com.dmg.niuniuserver.service.cache.GameWaterPoolCacheService;
import com.dmg.server.common.model.dto.GameRecordDTO;
import com.dmg.server.common.model.dto.UserResultDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mice
 * @description: 游戏房间
 * @return
 * @date 2019/7/1
 */
@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    @Autowired
    private PushService pushService;

    @Autowired
    private ALGService algService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private GameWaterPoolCacheService gameWaterPoolCacheService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MQProducer defautProducer;

    @Autowired
    private UserControlService userControlService;

    @Override
    public int getPlayerSeatId(GameRoom room, Player player) {
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer().getUserId().intValue() == player.getUserId().intValue()) {
                return seat.getSeatId();
            }
        }
        return 0;
    }

    @Override
    public GameRoom getRoom(int roomId) {
        return RoomManager.instance().getRoom(roomId);
    }

    @Override
    public void handleRoundEnd(GameRoom room) {
        GameRoom gameRoom = RoomManager.instance().getRoom(room.getRoomId());
        for (Seat seat : room.getSeatMap().values()) {
            Player player = this.playerService.getPlayer(seat.getPlayer().getUserId());
            if (null == player || player.getRoomId() != gameRoom.getRoomId()) {
                gameRoom.getSeatMap().remove(seat.getSeatId());
            }
            if (seat.getGameCount() > 0) {
                seat.setReady(false);
            }
        }
        gameRoom.setRoomStatus(RoomStatus.STATE_WAIT_ALL_READY);
        Object object = this.redisUtil.get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel());
        BigDecimal sysGold = new BigDecimal(object == null ? "0" : object.toString());
        Object pumpObj = this.redisUtil.get(RedisRegionConfig.FILE_PUMP_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel());
        BigDecimal pump = new BigDecimal(pumpObj == null ? "0" : pumpObj.toString());

        log.info("房间：{}，房间等级：{}，当前系统金币为：{},抽水金币为：{}", room.getRoomId(), room.getLevel(), sysGold, pump);
        // 重置数据，准备开始下一把
        this.resetRoomAndBegin(gameRoom);
    }

    /**
     * 重置房间数据，开始下一把游戏
     *
     * @param room
     */
    private void resetRoomAndBegin(GameRoom room) {
        room.clear();

        List<Seat> list = new ArrayList<>();
        boolean allRobot = true;
        for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
            Seat seat = entry.getValue();
            if (seat.getBalance() != null && seat.getPlayer() instanceof Robot) {
                seat.getPlayer().setWinLostGold(seat.getPlayer().getWinLostGold() + seat.getBalance().getWinscore().doubleValue());
            }
            if (seat.getChipsRemain() < room.getLowerLimit().doubleValue()) {
                seat.getPlayer().setLeaveReason(LeaveReason.LEAVE_NOMONEY);
                list.add(seat);
            }
            if (!(seat.getPlayer() instanceof Robot)) {
                Player player = this.playerService.getPlayer(seat.getPlayer().getUserId());
                seat.getPlayer().setOnline(player.isOnline());
                log.debug("==>userId:{},online:{}", player.getUserId(), player.isOnline());
                if (player.isOnline()) {
                    seat.getPlayer().setDisconnectTime(0);
                    allRobot = false;
                } else {
                    // 踢出掉线玩家
                    log.info("踢出房间{}的掉线玩家{}", room.getRoomId(), seat.getPlayer().getUserId());
                    seat.getPlayer().setLeaveReason(LeaveReason.LEAVE_OFFLINE);
                    list.add(seat);
                    RoomManager.instance().getPlayerRoomIdMap().remove(seat.getPlayer().getUserId());
                }
            }
            seat.clear(false, room);
            seat.setStatus(SeatState.STATE_WAIT_READY);
        }
        if (allRobot) {
            log.info("房间全是机器人 踢出");
            for (Seat seat : room.getSeatMap().values()) {
                seat.getPlayer().setLeaveReason(LeaveReason.LEAVE_NORMAL);
                this.quitRoom(seat.getPlayer(), false);
            }
        } else {
            for (Seat seat : list) {
                if (seat.getPlayer() instanceof Robot) {
                    // 踢掉机器人
                    seat.getPlayer().setLeaveReason(LeaveReason.LEAVE_NORMAL);
                }
                // 踢掉玩家
                this.quitRoom(seat.getPlayer(), false);
            }
        }

        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer() instanceof Robot && seat.getChipsRemain() >= room.getLowerLimit().doubleValue()) {
                try {
                    int time = RandomUtil.randomInt(room.getReadyTime());
                    TimerManager.instance().submitDelayWork(ReadyPlayerDelayWork.class, time, room.getRoomId(), seat.getSeatId());
                } catch (SchedulerException e) {
                    log.error("submit ReadyPlayerDelayWork error:{}", e);
                }
            }
        }
        try {
            if (!room.isPrivateRoom()) {
                TimerManager.instance().submitDelayWork(ReadyPlayerDelayWork.class, room.getReadyTime(), room.getRoomId(), 0);
            }
        } catch (SchedulerException e) {
            log.error("submit ReadyPlayerDelayWork error:{}", e);
        }
        room.setPhaseCountdown(System.currentTimeMillis() + room.getReadyTime());
    }

    @Override
    public RoomInfoDTO getRoomInfo(GameRoom room, Seat seats) {
        RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
        RoomInfoDTO.TableInfo tableInfo = new RoomInfoDTO.TableInfo();
        Map<String, RoomInfoDTO.SeatInfo> seatInfoMap = new ConcurrentHashMap<>();
        Map<String, RoomInfoDTO.PlayerInfo> playerInfoMap = new ConcurrentHashMap<>();
        tableInfo.setCurRound(room.getRound());
        tableInfo.setMaxRound(GameConfig.ROOM_MAX_TURNS);
        // tableInfo.setTopBet(room.getTotalBet());
        tableInfo.setRoomId(room.getRoomId());
        tableInfo.setRoomStatus(room.getRoomStatus());
        tableInfo.setRoomType(room.getRoomType());
        tableInfo.setCreatorId(room.getCreator());
        tableInfo.setCreateDate(room.getCreateTime());
        tableInfo.setRoomLevel(room.getLevel());
        tableInfo.setRoomLevelName(room.getLevelName());
        tableInfo.setLowerLimit(room.getLowerLimit());
        tableInfo.setUpperLimit(room.getUpperLimit());
        tableInfo.setBaseScore(room.getBaseScore());
        tableInfo.setTotalPlayer(room.getTotalPlayer());
        tableInfo.setCurPlayerNumber(room.getSeatMap().size());
        if (this.getActionType(room, null) != null) {
            tableInfo.setActionType(this.getActionType(room, null));
        }
        // 准备其他玩家空数据
        List<Poker> pokers = new ArrayList<>();
        Poker poker = new Poker();
        poker.setType(3);
        poker.setValue(14);
        if (seats != null && seats.getStatus() >= SeatState.KAIPAI) {
            for (int i = 0; i < 5; i++) {
                pokers.add(poker);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                pokers.add(poker);
            }
        }
        for (Seat seat : room.getSeatMap().values()) {
            RoomInfoDTO.SeatInfo seatInfo = new RoomInfoDTO.SeatInfo();
            RoomInfoDTO.PlayerInfo playerInfo = new RoomInfoDTO.PlayerInfo();
            if (seat.isReady() && seat.getGameCount() > 0 && seat.getStatus() < SeatState.STATE_WAIT_READY && seat.getStatus() != SeatState.STATE_WAIT_START) {
                seatInfo.setHandCards(pokers);
            }
            Player p = seat.getPlayer();
            // 如果是重连消息,或者后进入玩家的消息
            if (seats != null && seat.getSeatId() == seats.getSeatId()) {
                if (seat.getStatus() >= SeatState.KAIPAI) {
                    // 手牌
                    seatInfo.setHandCards(seat.send(2));
                } else {
                    seatInfo.setHandCards(seat.send(1));
                }
                seatInfo.setCardsNum(seat.getHand().size());
                seatInfo.setHandCardType(seat.getHandCardsType() == null ? 0 : seat.getHandCardsType().getValue());
                // 不是重连
            } else if (seats == null || seat.getSeatId() != seats.getSeatId()) {
                if (room.getOpenCards().get(seat.getSeatId()) != null && seat.getHand().size() > 0) {
                    if (seat.getStatus() >= SeatState.KAIPAI_OVER) {
                        seatInfo.setHandCards(seat.send(2));
                    } else {
                        seatInfo.setHandCards(pokers);
                    }
                    seatInfo.setCardsNum(seat.getHand().size());
                    seatInfo.setHandCardType(seat.getHandCardsType().getValue());
                } else if (room.getOpenCards().get(seat.getSeatId()) == null && seat.isHaveSeenCard()) {
                    seatInfo.setCardsNum(seat.getHand().size());
                }
            }
            seatInfo.setPlayerOnline(p.isOnline());
            seatInfo.setUserId(p.getUserId());
            seatInfo.setUserCode(p.getUserCode());
            seatInfo.setHasPlayed(seat.getGameCount() > 0);
            seatInfo.setGold(seat.getChipsRemain() != 0 ? seat.getChipsRemain() : p.getGold());
            seatInfo.setQiangZhuangMultiple(room.getBankerFraction().get(seat.getSeatId()) == null ? 0 : room.getBankerFraction().get(seat.getSeatId()));
            seatInfo.setBetChips(seat.getBetChips());
            seatInfo.setNickname(seat.getPlayer().getNickname());
            seatInfo.setSeatStatus(seat.getStatus());
            seatInfo.setSeatId(seat.getSeatId());
            seatInfo.setTrusteeship(false);
            seatInfo.setSeeCard(seat.isHaveSeenCard());
            seatInfo.setAutoBet(false);
            seatInfo.setReady(seat.isReady());
            seatInfo.setReadyToTime(room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY ? 0 : room.getReadyTime() / 1000);
            seatInfo.setTotalBet(seat.getBetChips());
            if (room.getBankerSeat() != null && room.getBankerSeat().getSeatId() == seat.getSeatId()) {
                seatInfo.setBankerFlag(true);
                seatInfo.setQiangZhuangMultiple(room.getBankerMultiple());
            } else {
                seatInfo.setBankerFlag(false);
            }
            seatInfo.setActionType(this.getActionType(room, seat));
            if (room.getRoomStatus() == RoomStatus.STATE_XIAZHU) {
                seatInfo.setBetChips(seat.getBetChips());
            }
            if (room.getRoomStatus() == RoomStatus.STATE_QIANG_ZHUANG) {
                seatInfo.setQiangZhuangMultiple(room.getBankerFraction().get(seat.getSeatId()));
            }
            seatInfoMap.put(seat.getSeatId() + "", seatInfo);

            playerInfo.setGameCount(seat.getGameCount());
            playerInfo.setUserId(p.getUserId());
            playerInfo.setUserCode(p.getUserCode());
            playerInfo.setNickname(p.getNickname());
            playerInfo.setHeadImage(p.getHeadImg() == null ? "" : p.getHeadImg());
            playerInfo.setSex(p.getSex());
            playerInfo.setGold(seat.getChipsRemain() != 0 ? seat.getChipsRemain() : p.getGold());
            playerInfoMap.put(seat.getSeatId() + "", playerInfo);
        }
        roomInfoDTO.setTableInfo(tableInfo);
        roomInfoDTO.setSeatInfoMap(seatInfoMap);
        roomInfoDTO.setPlayerInfoMap(playerInfoMap);
        return roomInfoDTO;
    }

    @Override
    public Integer getActionType(GameRoom room, Seat seat) {
        switch (room.getRoomStatus()) {
            case RoomStatus.STATE_QIANG_ZHUANG:
                return ActionType.TYPE_QIANGZHUANG;
            case RoomStatus.STATE_DING_ZHUANG:
                return ActionType.TYPE_DING_ZHUANG;
            case RoomStatus.STATE_XIAZHU:
                if (seat != null && seat.getSeatId() == room.getBankerSeat().getSeatId()) {
                    return ActionType.TYPE_DING_ZHUANG;
                }
                return ActionType.TYPE_XIAZHU;
            case RoomStatus.STATE_KAIPAI:
                return ActionType.TYPE_KAIPAI;
            case RoomStatus.STATE_SETTLEMENT:
                return ActionType.TYPE_KAIPAI;
            default:
                return ActionType.TYPE_UNKNOWN;
        }
    }

    @Override
    public SeatMsgDTO getSeatInfo(GameRoom room, int seatId) {
        SeatMsgDTO seatMsg = new SeatMsgDTO();
        Seat seat = room.getSeatMap().get(seatId);
        Player p = seat.getPlayer();
        seatMsg.setSeatStatus(seat.getStatus());
        seatMsg.setHandCards(seat.getHand());
        seatMsg.setPlayerOnline(p.isOnline());
        seatMsg.setUserId(p.getUserId());
        seatMsg.setGold(seat.getChipsRemain() != 0 ? seat.getChipsRemain() : p.getGold());
        seatMsg.setCardsNum(seat.getHand().size());
        seatMsg.setSeatId(seatId);
        seatMsg.setTrusteeship(false);
        seatMsg.setSeeCard(seat.isHaveSeenCard());
        seatMsg.setAutoBet(false);
        seatMsg.setReady(seat.isReady());
        seatMsg.setReadyToTime(room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY ? 0 : room.getPhaseCountdown());
        seatMsg.setTotalBet(seat.getBetChips());
        return seatMsg;
    }

    @Override
    public void enterDeal(GameRoom room) {
        // 设置进入发牌阶段
        room.setPhaseCountdown(System.currentTimeMillis());
        RoomInfoDTO tableInfo = this.getRoomInfo(room, null);
        MessageResult messageResult = new MessageResult(ResultEnum.SUCCESS.getCode(), tableInfo, MessageConfig.GAME_START);
        this.pushService.broadcast(messageResult, room);
        // 发牌
        log.info("房间：{}，开始发牌", room.getRoomId());
        this.dealPoker(room);
    }

    /**
     * 发牌
     *
     * @param room
     */
    private void dealPoker(GameRoom room) {
        // 初始扑克
        LinkedList<Poker> deck = this.algService.createDeck(room);

        // 开始发牌
        List<Seat> seatList = CollUtil.newCopyOnWriteArrayList(room.getSeatMap().values());
        List<BrandDTO> brandDTOList = new ArrayList<>();
        List<Long> playerList = new ArrayList<>();
        List<Long> robotList = new ArrayList<>();
        for (Seat seat : room.getSeatMap().values()) {
            if (!seat.isReady() || seat.getGameCount() <= 0) {
                continue;
            }
            if (!(seat.getPlayer() instanceof Robot)) {
                playerList.add(seat.getPlayer().getUserId());
            } else {
                robotList.add(seat.getPlayer().getUserId());
            }
            BrandDTO brandDTO = new BrandDTO();
            // 每人5张牌
            List<Poker> hand = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Poker poker = deck.pop();
                hand.add(poker);
            }
            brandDTO.setHand(hand);
            Combination type = this.algService.evalPokerType(hand, seat, room);
            log.info("发牌房间：{}，扑克牌：{}，牌型：{}", room.getRoomId(), hand.toString(), type.getValue());
            brandDTO.setCombination(type);
            brandDTOList.add(brandDTO);
        }
        // 排序
        Collections.sort(brandDTOList);// .sort((x, y) -> Integer.compare(x.getCombination().getValue(),
        // y.getCombination().getValue()));
        log.info("排序完成：{}", brandDTOList.toString());
        // 查询水池配置
        Object object = this.redisUtil.get(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel());
        BigDecimal sysGold = new BigDecimal(object == null ? "0" : object.toString());
        log.info("房间：{}，房间等级：{}，当前系统金币：{}", room.getRoomId(), room.getLevel(), sysGold);
        GameWaterPoolDTO gameWaterPoolDTO = this.gameWaterPoolCacheService.getGameWaterPool(sysGold, Constant.GAME_ID, room.getLevel());
        log.info("房间：{}，game-config查询水池配置：{}", room.getRoomId(), gameWaterPoolDTO.toString());
        Integer probability = gameWaterPoolDTO.getProbabilityBasics() + gameWaterPoolDTO.getProbabilityIncrease() * playerList.size();
        //查询玩家点控自控
        if (playerList.size() > 0) {
            log.info("房间：{}，调用game-config查询用户控制模型req data:{}", room.getRoomId(), playerList.toString());
            try {
                Map<Long, UserControlInfoDTO> result = userControlService.getUserControlModel(playerList);
                log.info("房间：{}，调用game-config查询用户控制模型resp data:{}", room.getRoomId(), result.toString());
                if (result != null && result.size() > 0) {
                    room.setUserControlInfoDTOMap(result);
                    result.values().forEach(userControlInfoDTO -> {
                        Boolean isWin = GameOddsEnum.getIsWinByIcon(userControlInfoDTO.getModel());
                        if (gameWaterPoolDTO.getIsSystem() ? isWin : !isWin) {
                            robotList.add(userControlInfoDTO.getUserId());
                            playerList.remove(userControlInfoDTO.getUserId());
                        }
                    });
                    probability = gameWaterPoolDTO.getProbabilityBasics()
                            + gameWaterPoolDTO.getProbabilityIncrease() * (gameWaterPoolDTO.getIsSystem() ? robotList.size() : playerList.size());
                }
            } catch (Exception e) {
                log.info("房间：{}，调用game-config查询用户控制模型出现异常:{}", room.getRoomId(), e);
            }
        }
        log.info("房间：{}，控制概率：{}", room.getRoomId(), probability);
        // 概率击中
        Long playerMax = null;
        if (RandomUtil.randomInt(10000) + 1 <= probability) {
            if (gameWaterPoolDTO.getIsSystem() && robotList != null && robotList.size() > 0) {
                playerMax = robotList.get(RandomUtil.randomInt(0, robotList.size()));
            } else {
                if (playerList != null && playerList.size() > 0) {
                    playerMax = playerList.get(RandomUtil.randomInt(0, playerList.size()));
                } else {
                    playerMax = robotList.get(RandomUtil.randomInt(0, robotList.size()));
                }
            }
            log.info("房间：{}，击中player:{}", room.getRoomId(), playerMax);
        }
        // 拿最大牌
        Boolean isMax = false;
        for (Seat seat : room.getSeatMap().values()) {
            if (!seat.isReady() || seat.getGameCount() <= 0) {
                continue;
            }
            Integer i;
            if (!isMax && playerMax != null && seat.getPlayer().getUserId().longValue() == playerMax.longValue()) {
                i = brandDTOList.size() - 1;
                isMax = true;
            } else {
                if (isMax || playerMax == null) {
                    i = RandomUtil.randomInt(0, brandDTOList.size());
                } else {
                    if (brandDTOList.size() == 1) {
                        i = 0;
                    } else {
                        i = RandomUtil.randomInt(0, brandDTOList.size() - 1);
                    }
                }
            }
            BrandDTO brandDTO = brandDTOList.get(i);
            brandDTOList.remove(brandDTO);
            log.info("用户:{},手牌：{},手牌牌型:{}", seat.getPlayer().getUserId(), brandDTO.getHand().toString(), brandDTO.getCombination().getValue());
            // 设置 WholeTenCards
            this.algService.setOrdinaryCattleByNoWang(brandDTO.getHand(), seat);
            // 设置手牌牌型
            seat.setHandCardsType(brandDTO.getCombination());
            // 设置手牌
            seat.setHand(brandDTO.getHand());
            // 玩家手牌记录(本局)
            seat.addCacheGameInfo(brandDTO.getHand(), brandDTO.getCombination(), room.getRound());
            // 推送发牌消息
            this.pushDealMSG(seatList, seat, 1);
        }
        // 通知下一步操作,如果是抢庄
        this.sendInfoBanker(room);
    }

    /**
     * 通知抢庄操作
     *
     * @author CharlesLee
     * @date 2018/4/13 0013 14:19
     */
    private void sendInfoBanker(GameRoom room) {
        Map<String, Object> actionInfo = new HashMap<>();
        long times = System.currentTimeMillis() + GameConfig.QIANG_ZHUANG_LENGTH_TIME;
        actionInfo.put("actionType", ActionType.TYPE_QIANGZHUANG);
        actionInfo.put("countdownTime", times);
        actionInfo.put("qiangZhuangMultiple", room.betMultiple);

        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer() != null && seat.isReady() && seat.getGameCount() > 0) {
                seat.setStatus(SeatState.QIANGZHUANG);
                MessageResult messageResult = new MessageResult(MessageConfig.TURN_ACTION, actionInfo);
                this.pushService.push(seat.getPlayer().getUserId(), messageResult);
            }
        }
        // 提交抢庄操作的异步任务
        try {
            // 当前状态修改成为抢庄
            room.setRoomStatus(RoomStatus.STATE_QIANG_ZHUANG);
            room.setPhaseCountdown(times);
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getPlayer() instanceof Robot) {
                    int time = RandomUtil.randomInt(GameConfig.QIANG_ZHUANG_LENGTH_TIME);
                    TimerManager.instance().submitDelayWork(ToRobBankerWork.class, time, room.getRoomId(), seat.getSeatId());
                }
            }
            TimerManager.instance().submitDelayWork(ToRobBankerWork.class, GameConfig.QIANG_ZHUANG_LENGTH_TIME, room.getRoomId(), 0);
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(String.valueOf(e));
        }
    }

    /**
     * 发牌消息推送
     *
     * @param seat
     */
    @Override
    public void pushDealMSG(List<Seat> seats, Seat seat, int count) {
        List<SendCardsMsgDTO> sendCardsMsgDTOS = new ArrayList<>();
        List<Poker> pokers = new ArrayList<>();
        // 准备其他玩家空数据
        for (int i = 0; i < 3 + count; i++) {
            Poker poker = new Poker();
            poker.setType(3);
            poker.setValue(14);
            pokers.add(poker);
        }
        for (Seat seat1 : seats) {
            if (seat1.getSeatId() == seat.getSeatId()) {
                Player player = seat.getPlayer();
                SendCardsMsgDTO sendCardsMsgDTO = new SendCardsMsgDTO();
                sendCardsMsgDTO.setUserId(player.getUserId());
                sendCardsMsgDTO.setCurActionSeatId(seat.getSeatId());
                sendCardsMsgDTO.setHandCards(seat.send(count));
                sendCardsMsgDTO.setDealCount(count);
                sendCardsMsgDTOS.add(sendCardsMsgDTO);
            } else if (seat1.isReady()) {
                Player player = seat1.getPlayer();
                SendCardsMsgDTO sendCardsMsgDTO = new SendCardsMsgDTO();
                sendCardsMsgDTO.setUserId(player.getUserId());
                sendCardsMsgDTO.setCurActionSeatId(seat1.getSeatId());
                sendCardsMsgDTO.setHandCards(pokers);
                sendCardsMsgDTO.setDealCount(count);
                sendCardsMsgDTOS.add(sendCardsMsgDTO);
            }
        }
        MessageResult messageResult = new MessageResult(ResultEnum.SUCCESS.getCode(), sendCardsMsgDTOS, MessageConfig.GAME_SEND_CARDS);
        this.pushService.push(seat.getPlayer().getUserId(), messageResult);
    }

    /**
     * 记录战绩
     *
     * @param room
     * @param recordDataDTO
     */
    @SuppressWarnings("unchecked")
    private void recordData(GameRoom room, RecordDataDTO recordDataDTO) {
        // 发送结算数据
        String roundCode = RoundIdUtils.getGameRecordId(String.valueOf(Constant.GAME_ID), String.valueOf(room.getRoomId()));
        recordDataDTO.getBalanceList().forEach(balance -> {
            try {
                UserControlInfoDTO userControlInfoDTO = room.getUserControlInfoDTOMap().get(balance.getUserId());
                this.defautProducer.send(JSON.toJSON(GameRecordDTO.<UserResultDTO<Poker>>builder()
                        .gameId(Constant.GAME_ID)
                        .gameDate(new Date())
                        .userId(balance.getUserId())
                        .userName(balance.getNickname())
                        .fileId(room.getLevel())
                        .fileName(room.getLevelName())
                        .baseScore(room.getBaseScore())
                        .afterGameGold(balance.getTotalScore())
                        .beforeGameGold(balance.getTotalScore().subtract(balance.getWinscore()))
                        .betsGold(balance.getBets())
                        .winLosGold(balance.getWinscore())
                        .serviceCharge(balance.getPumpMoney())
                        .isRobot(balance.getRobot())
                        .roundCode(roundCode)
                        .controlState((userControlInfoDTO != null && userControlInfoDTO.getControlState().intValue() == UserControlStateEnum.CONTROL_STATE_POINT.getCode().intValue()))
                        .gameResult(recordDataDTO.getUserResult()).build()).toString());
            } catch (Exception e) {
                log.error("战绩信息发送异常:{}", e);
            }
        });
    }

    @Override
    public boolean quitRoom(Player player, boolean bool) {
        GameRoom room = this.getRoom(player.getRoomId());
        if (room == null) {
            this.pushService.push(player.getUserId(), MessageConfig.PLAYER_LEAVE_ROOM, ResultEnum.ROOM_HAS_NO_EXIST.getCode());
            return false;
        }
        int seat = this.getPlayerSeatId(room, player);
        if (seat == 0) {
            room.getWatchList().remove(player);
            this.pushService.push(player.getUserId(), MessageConfig.PLAYER_LEAVE_ROOM, ResultEnum.PLAYER_HAS_NOT_SEAT.getCode());
            this.playerService.syncRoom(0, player.getUserId());
            return true;
        } else {
            if (player instanceof Robot) {
                player.setPlayRounds(0);
                player.setWinLostGold(new Double("0"));
            }
            if (bool) {
                room.getSeatMap().remove(seat);
                Map<String, Object> map = new HashMap<>();
                map.put("userId", player.getUserId());
                map.put("leaveReason", 4);
                MessageResult messageResult = new MessageResult(MessageConfig.PLAYER_LEAVE_ROOM_NTC, map);
                this.pushService.push(player.getUserId(), messageResult);
                if (player instanceof Robot) {
                    player.setRoomId(0);
                } else {
                    this.playerService.syncRoom(0, player.getUserId());
                    if (player.isOnline()) {
                        GameOnlineChangeUtils.decOnlineNum(Constant.GAME_ID, room.getLevel());
                    }
                }
                room.getWatchList().remove(player);
            } else {
                if (room.getRoomStatus() <= RoomStatus.STATE_WAIT_ALL_READY || (room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY && !room.getSeatMap().get(seat).isReady())) {
                    room.getSeatMap().remove(seat);

                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", player.getUserId());
                    map.put("leaveReason", player.getLeaveReason());
                    MessageResult messageResult = new MessageResult(MessageConfig.PLAYER_LEAVE_ROOM_NTC, map);
                    this.pushService.push(player.getUserId(), messageResult);
                    if (player instanceof Robot) {
                        player.setRoomId(0);
                    } else {
                        this.playerService.syncRoom(0, player.getUserId());
                        if (player.isOnline()) {
                            GameOnlineChangeUtils.decOnlineNum(Constant.GAME_ID, room.getLevel());
                        }
                    }
                    // 通知其他玩家此玩家退出房间
                    this.pushService.broadcast(messageResult, room);
                    this.canEnterDeal(room);
                    room.getWatchList().remove(player);
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("runawayMoney", GameConfig.RUN_AWAY_BET * room.getBaseScore().doubleValue());
                    MessageResult messageResult = new MessageResult(MessageConfig.PLAYER_LEAVE_ROOM, map);
                    this.pushService.push(player.getUserId(), messageResult);
                    return false;
                }

            }
        }
        if (!(player instanceof Robot)) {
            RoomManager.instance().getPlayerRoomIdMap().remove(player.getUserId());
        }
        room.setPlayerNumber(room.getPlayerNumber() - 1);
        log.info("==>玩家{},退出房间{},理由{}", player.getUserId(), room.getRoomId(), player.getLeaveReason());
        return true;
    }

    @Override
    public void answerDissolveRoom(Player player, boolean agree) {
        GameRoom room = this.getRoom(player.getRoomId());
        Map<Integer, Boolean> dissolveMap = room.getDissolveMap();
        int seat = this.getPlayerSeatId(room, player);
        dissolveMap.put(seat, agree);
        MessageResult messageResult = new MessageResult(MessageConfig.ANSWER_DISBAND_ROOM, dissolveMap);
        this.pushService.push(player.getUserId(), messageResult);
        Map<String, Object> map = new HashMap<>();
        map.put("userId", player.getUserId());
        map.put("agree", agree);
        messageResult = new MessageResult(MessageConfig.ANSWER_DISBAND_ROOM_NTC, map);
        this.pushService.broadcast(messageResult, room);
        log.debug("===dissolve,seat:{},agree:{}", seat, agree);
        if (agree) {
            int agreePlayer = 0;
            for (Boolean bool : dissolveMap.values()) {
                if (bool) {
                    agreePlayer += 1;
                }
            }
            int plyaerCount = 0;
            for (Seat s : room.getSeatMap().values()) {
                if (s.getGameCount() > 0) {
                    plyaerCount++;
                }
            }
            if (agreePlayer >= plyaerCount) {
                room.setDissolveTime(0);
                room.setDissolveUserId(0);
                this.resultAll(room);
            }
        } else {
            room.setDissolveTime(0);
            room.setDissolveUserId(0);
            dissolveMap.clear();
            Map<String, Object> ret = new HashMap<>();
            ret.put("dissolve", false);
            messageResult = new MessageResult(MessageConfig.SUCCESS_DISBAND_ROOM_NTC, ret);
            this.pushService.broadcast(messageResult, room);
            try {
                TimerManager.instance().stopTimeWork(room.getDissolveTaskId());
            } catch (SchedulerException e) {
                log.error("stop dissolve task fail:{}", e);
            }
        }
    }

    @Override
    public void chat(Player player, JSONObject message) {
        GameRoom room = this.getRoom(player.getRoomId());
        if (room == null) {
            return;
        }
        MessageResult messageResult = new MessageResult(MessageConfig.GAME_CHAT_MSGNTC, message);
        this.pushService.broadcast(messageResult, room);
    }

    /**
     * 定庄事件
     *
     * @author CharlesLee
     * @date 2018/4/17 0017 11:53
     */
    @Override
    public synchronized void definitionBanker(GameRoom room, Seat seat) {
        log.debug("房间{},进入定庄环节", room.getRoomId());
        if (room.isPrivateRoom() && (room.getCustomRule().getGamePlay() == 1 || room.getCustomRule().getGamePlay() == 3)) {
            if (room.getBankerSeat() != null) {
                log.error("房间{}处于已定庄状态", room.getRoomId());
                return;
            }
        }
        // 设置成为定庄阶段状态
        room.setRoomStatus(RoomStatus.STATE_DING_ZHUANG);
        // 直接定庄
        List<Integer> curActionSeatIdList = new ArrayList<>();
        if (seat != null) {
            room.setBankerSeat(seat);
            room.setBankerMultiple(1);
        } else {
            if (room.getBankerFraction().size() == 0 || room.getNoBankerFraction() == room.getplayerReadyCount()) {
                // 随机定庄
                List<Integer> seatIdList = new ArrayList<>(room.getSeatMap().keySet());
                if (seatIdList.size() == 1) {
                    curActionSeatIdList.add(seatIdList.get(0));
                    room.setBankerSeat(room.getSeatMap().get(seatIdList.get(0)));
                } else {
                    for (Seat temp : room.getSeatMap().values()) {
                        if (temp != null && temp.isReady() && temp.getGameCount() > 0) {
                            curActionSeatIdList.add(temp.getSeatId());
                        }
                    }
                    for (; ; ) {
                        Integer random = seatIdList.get(new Random().nextInt(room.getSeatMap().size()));
                        Seat bankerSeat = room.getSeatMap().get(random);
                        if (bankerSeat != null && bankerSeat.isReady() && bankerSeat.getGameCount() > 0) {
                            room.setBankerSeat(bankerSeat);
                            break;
                        }
                    }
                }
                room.setBankerMultiple(1);
            } else {
                int max = 0;
                for (Entry<Integer, Integer> entry : room.getBankerFraction().entrySet()) {
                    int lin = entry.getValue();
                    if (lin > max) {
                        max = lin;
                    }
                }
                if (max == 0) {
                    room.getBankerFraction().clear();
                    log.debug("定庄5,房间号为{}", room.getRoomId());
                    this.definitionBanker(room, null);
                } else {
                    List<Integer> arrayCopy = new ArrayList<>();
                    for (Entry<Integer, Integer> entry : room.getBankerFraction().entrySet()) {
                        if (entry.getValue() == max) {
                            arrayCopy.add(entry.getKey());
                        }
                    }
                    if (arrayCopy.size() == 1) {
                        curActionSeatIdList.add(arrayCopy.get(0));
                        room.setBankerSeat(room.getSeatMap().get(arrayCopy.get(0)));
                    } else {
                        for (Integer temp : arrayCopy) {
                            curActionSeatIdList.add(temp);
                        }
                        for (; ; ) {
                            Integer random = arrayCopy.get(new Random().nextInt(arrayCopy.size()));
                            Seat bankerSeat = room.getSeatMap().get(random);
                            if (bankerSeat != null && bankerSeat.isReady() && bankerSeat.getGameCount() > 0) {
                                room.setBankerSeat(bankerSeat);
                                break;
                            }
                        }
                    }
                    room.setBankerMultiple(max);
                }
            }
        }
        this.sendBankerInfo(room, seat == null, curActionSeatIdList);
        log.debug("房间{},定庄结束", room.getRoomId());
    }

    private void sendBankerInfo(GameRoom room, boolean next, List<Integer> curActionSeatIdList) {
        // 定庄完成消息推送
        BankerInfoDTO bankerInfoDTO = new BankerInfoDTO();
        bankerInfoDTO.setUserId(room.getBankerSeat().getPlayer().getUserId());
        bankerInfoDTO.setSeatId(room.getBankerSeat().getSeatId());
        bankerInfoDTO.setBankerMultiple(room.getBankerMultiple());
        bankerInfoDTO.setActionType(ActionType.TYPE_DING_ZHUANG);
        bankerInfoDTO.setCurActionSeatIdList(curActionSeatIdList);
        MessageResult messageResult = new MessageResult(MessageConfig.DO_ACTION_RESULT_NTC, bankerInfoDTO);
        this.pushService.broadcast(messageResult, room);
        // 启动通知任务, 专门用作与进入下一个阶段,该阶段用来通知客户端进入下注
        if (next) {
            try {
                // 清空抢庄数据
                // room.setBankerFraction(new HashMap<>());
                int time = GameConfig.DING_ZHUANG_LENGTH_TIME;
                if (curActionSeatIdList.size() == 1) {
                    time = 200;
                }
                room.setPhaseCountdown(System.currentTimeMillis() + time);
                TimerManager.instance().submitDelayWork(NotifyPlayerNoteCountdownTask.class, time, room.getRoomId());
            } catch (SchedulerException e) {
                log.error("submit NotifyPlayerNoteCountdown error:{}", e);
            }
        }
    }

    private boolean judgewinner(Seat seat, Seat beSeat, GameRoom room) {
        if (seat.getHandCardsType().getValue() > beSeat.getHandCardsType().getValue()) {
            return true;
        } else if (seat.getHandCardsType().getValue() < beSeat.getHandCardsType().getValue()) {
            return false;
        } else {
            return seat.getHandCardsType().getValue() == beSeat.getHandCardsType().getValue() && seat.getSeatId() == room.getBankerSeat().getSeatId();
        }
    }

    /**
     * 结算
     */
    @Override
    public synchronized void settlement(int roomId) {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null || room.getRoomStatus() == RoomStatus.STATE_SETTLEMENT) {
            return;
        }
        room.setRoomStatus(RoomStatus.STATE_SETTLEMENT);
        log.debug("==>房间{},进入结算环节", room.getRoomId());
        Map<String, Balance> balanceMap = new HashMap<>();
        Seat bank = room.getBankerSeat();
        boolean isRobotBanker = false;
        if (bank.getPlayer() instanceof Robot) {
            isRobotBanker = true;
        }
        bank.setBalance(new Balance(bank.getPlayer().getUserId(), room.getRound(), bank.getPlayer().getNickname(), bank.getSeatId(), 1));
        Balance bankBalance = bank.getBalance();
        bankBalance.setHandCards(bank.getHand());
        bankBalance.setHandCardType(bank.getHandCardsType().getValue());
        // 庄家金币变化
        BigDecimal bankChangeGold = BigDecimal.ZERO;
        BigDecimal bankBets = BigDecimal.ZERO;
        List<Object> pokerList = new ArrayList<>();

        Map<Player, BigDecimal> tResultMap = new HashMap<>();
        List<Object> winList = new ArrayList<>();
        List<UserResultDTO<Poker>> userResultDTOList = new ArrayList<>();
        List<Balance> balanceList = new ArrayList<>();
        for (Seat seat : room.getSeatMap().values()) {
            if (bank.getSeatId() == seat.getSeatId() || !seat.isReady() || seat.getGameCount() <= 0) {
                continue;
            }
            if (!CollUtil.isEmpty(seat.getHand())) {
                List<Poker> ps = seat.getHand();
                for (Poker p : ps) {
                    pokerList.add(p.fullValue());
                }
            }
            Balance seatBalance = seat.getBalance();
            if (seatBalance == null) {
                seatBalance = new Balance(seat.getPlayer().getUserId(), room.getRound(), seat.getPlayer().getNickname(), seat.getSeatId(), 0);
                seatBalance.setHandCards(seat.getHand());
                seatBalance.setHandCardType(seat.getHandCardsType().getValue());
            }
            // 闲家金币变化
            BigDecimal seatChangeGold = BigDecimal.ZERO;
            BigDecimal pumpMoney = BigDecimal.ZERO;
            // 如果庄家赢了
            if (this.judgewinner(bank, seat, room)) {
                // 计算下注金额,为底分 * 抢庄倍数 * 赢加牌型倍数
                BigDecimal money = room.getBaseScore().multiply(BigDecimal.valueOf(room.getBankerMultiple() * room.getMul(bank.getHandCardsType().getValue()) * seat.getBetChips()));
                if (!room.isPrivateRoom() && seat.getChipsRemain() < money.doubleValue()) {
                    money = BigDecimal.valueOf(seat.getChipsRemain());
                }
                // 庄家赢
                bankChangeGold = bankChangeGold.add(money);
                // 闲家输
                seat.setChipsRemain(seat.getChipsRemain() - money.doubleValue());
                seatBalance.setWinscore(seatBalance.getWinscore().subtract(money));
                seatChangeGold = seatChangeGold.subtract(money);
                // 添加系统金币池
                if (seat.getPlayer() instanceof Robot) {
                    this.redisUtil.decr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel(), money.doubleValue());
                }
            } else {
                BigDecimal money = room.getBaseScore().multiply(BigDecimal.valueOf(room.getBankerMultiple() * room.getMul(seat.getHandCardsType().getValue()) * seat.getBetChips()));
                if (!room.isPrivateRoom() && bank.getChipsRemain() < money.doubleValue()) {
                    money = BigDecimal.valueOf(bank.getChipsRemain());
                }
                // 庄家输
                bankChangeGold = bankChangeGold.subtract(money);
                // 闲家赢
                // 抽水
                pumpMoney = money.multiply(BigDecimal.valueOf(room.getPumpRate())).divide(BigDecimal.valueOf(100));
                seat.setChipsRemain(seat.getChipsRemain() + money.doubleValue() - pumpMoney.doubleValue());
                seatBalance.setWinscore(seatBalance.getWinscore().add(money).subtract(pumpMoney));
                seatChangeGold = seatChangeGold.add(money).subtract(pumpMoney);
                log.info("闲家赢系统结算数据:seat:{},money:{},抽水：{}", seat.getPlayer().getUserId(), money, pumpMoney);
                // 系统抽水
                this.redisUtil.incr(RedisRegionConfig.FILE_PUMP_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel(), pumpMoney.doubleValue());
                // 添加系统金币池
                if (seat.getPlayer() instanceof Robot) {
                    this.redisUtil.incr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel(), money.subtract(pumpMoney).doubleValue());
                }
            }
            userResultDTOList.add(UserResultDTO.<Poker>builder()
                    .pokers(seat.getHand())
                    .pokerType(String.valueOf(seat.getHandCardsType().getValue()))
                    .userId(Integer.parseInt(String.valueOf(seat.getPlayer().getUserId())))
                    .winLosGold(seatChangeGold)
                    .mul(room.getMul(seat.getHandCardsType().getValue()))
                    .pressure(Integer.parseInt(String.valueOf(seat.getBetChips())))
                    .isRobot((seat.getPlayer() instanceof Robot))
                    .build());
            seatBalance.setTotalScore(BigDecimal.valueOf(seat.getChipsRemain()).setScale(2, BigDecimal.ROUND_HALF_UP));
            seat.getBalanceList().add(seatBalance);
            balanceMap.put(seat.getSeatId() + "", seatBalance);
            seat.setBalance(seatBalance);
            if (!room.isPrivateRoom()) {
                Player player = seat.getPlayer();
                if (!(seat.getPlayer() instanceof Robot)) {
                    player = this.playerService.getPlayer(seat.getPlayer().getUserId());
                }
                if (player.getGold() + seatChangeGold.doubleValue() + seat.getBetChips() < 0) {
                    player.setGold(0);
                } else {
                    player.setGold(player.getGold() + seatChangeGold.doubleValue() + seat.getBetChips());
                }
                log.debug("==>同步玩家{}金币", player.getUserId());
                this.playerService.update(player);
                seatBalance.setPumpMoney(pumpMoney);
                seatBalance.setBets(room.getBaseScore().multiply(BigDecimal.valueOf(room.getBankerMultiple() * seat.getBetChips())));
                bankBets = bankBets.add(seatBalance.getBets());
                if (!(seat.getPlayer() instanceof Robot)) {
                    seatBalance.setRobot(false);
                    tResultMap.put(player, seatChangeGold);
                    if (seatChangeGold.compareTo(BigDecimal.ZERO) > 0) {
                        winList.add(seat.getSeatId());
                    }
                }
                balanceList.add(seatBalance);
            }
        }
        for (Entry<Player, BigDecimal> entry : tResultMap.entrySet()) {
            Player player = entry.getKey();
            BigDecimal seatChangeGold = entry.getValue();
            this.playerService.increaseGold(seatChangeGold, player.getUserId());
        }

        log.info("庄家金币变化：{}", bankChangeGold);
        bank.setChipsRemain(bank.getChipsRemain() + bankChangeGold.doubleValue());
        bankBalance.setWinscore(bankBalance.getWinscore().add(bankChangeGold));
        BigDecimal pumpMoney = BigDecimal.ZERO;
        if (bankChangeGold.compareTo(BigDecimal.ZERO) > 0) {
            // 抽水
            pumpMoney = bankChangeGold.multiply(BigDecimal.valueOf(room.getPumpRate())).divide(BigDecimal.valueOf(100));
            log.info("庄家赢系统结算数据:seat:{},bankChangeGold:{},抽水：{}", bank.getPlayer().getUserId(), bankChangeGold, pumpMoney);
            bank.setChipsRemain(bank.getChipsRemain() - pumpMoney.doubleValue());
            bankBalance.setWinscore(bankBalance.getWinscore().subtract(pumpMoney));
            bankChangeGold = bankChangeGold.subtract(pumpMoney);
            // 系统抽水
            this.redisUtil.incr(RedisRegionConfig.FILE_PUMP_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel(), pumpMoney.doubleValue());
        }
        userResultDTOList.add(UserResultDTO.<Poker>builder()
                .pokers(bank.getHand())
                .pokerType(String.valueOf(bank.getHandCardsType().getValue()))
                .userId(Integer.parseInt(String.valueOf(bank.getPlayer().getUserId())))
                .mul(room.getMul(bank.getHandCardsType().getValue()))
                .rob(room.getBankerMultiple())
                .isBank(true)
                .isSys(isRobotBanker)
                .isRobot(isRobotBanker)
                .winLosGold(bankChangeGold).build());
        if (isRobotBanker) {
            if (bankChangeGold.compareTo(BigDecimal.ZERO) > 0) {
                this.redisUtil.incr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel(), bankChangeGold.doubleValue());
            }
            if (bankChangeGold.compareTo(BigDecimal.ZERO) < 0) {
                this.redisUtil.decr(RedisRegionConfig.FILE_WIN_GOLD_KEY + ":" + Constant.GAME_ID + "_" + room.getLevel(), bankChangeGold.negate().doubleValue());
            }
        }
        bankBalance.setTotalScore(BigDecimal.valueOf(bank.getChipsRemain()).setScale(2, BigDecimal.ROUND_HALF_UP));
        bank.getBalanceList().add(bankBalance);
        balanceMap.put(bank.getSeatId() + "", bankBalance);

        if (!room.isPrivateRoom()) {
            // 同步庄家数据
            Player player = bank.getPlayer();
            if (!(bank.getPlayer() instanceof Robot)) {
                player = this.playerService.getPlayer(bank.getPlayer().getUserId());
            }
            if (player.getGold() + bankChangeGold.doubleValue() < 0) {
                player.setGold(0);
            } else {
                player.setGold(player.getGold() + bankChangeGold.doubleValue());
            }
            this.playerService.update(player);
            bankBalance.setPumpMoney(pumpMoney);
            bankBalance.setBets(bankBets);
            if (!(bank.getPlayer() instanceof Robot)) {
                bankBalance.setRobot(false);
                List<Object> dataList = new ArrayList<>();
                dataList.addAll(bank.getHand());
                this.playerService.increaseGold(bankChangeGold, player.getUserId());
            }
            balanceList.add(bankBalance);
        }
        MessageResult messageResult = new MessageResult(MessageConfig.GAME_RESULT_MSG, balanceMap);
        this.pushService.broadcast(messageResult, room);
        // 记录战绩
        this.recordData(room, RecordDataDTO.builder().balanceList(balanceList).userResult(userResultDTOList).build());
        this.jionNextWork(room);
    }

    /**
     * 结算任务的下一步
     */
    private void jionNextWork(GameRoom gameRoom) {
        // 启动下一步操作, 下一步操作为重置数据,并通知客户端准备
        try {
            if (gameRoom.isPrivateRoom() && gameRoom.getCustomRule().getGamePlay() == 5 && gameRoom.getBankerSeat().getChipsRemain() <= 0 && gameRoom.getCustomRule().getCarryFraction() > 0) {
                this.resultAll(gameRoom);
                return;
            }
            if (!gameRoom.isPrivateRoom() || (gameRoom.isPrivateRoom() && gameRoom.getRound() < gameRoom.getTotalRound())) {
                gameRoom.setPhaseCountdown(GameConfig.XIA_JU_LENGTH_TIME);
                TimerManager.instance().submitDelayWork(BalanceResultDelayWork.class, GameConfig.XIA_JU_LENGTH_TIME, gameRoom.getRoomId());
            } else {
                // 总结算
                this.resultAll(gameRoom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成总结算数据
     */
    @Override
    public synchronized void resultAll(GameRoom room) {
        // 启动总结算任务
        try {
            TimerManager.instance().submitDelayWork(ResultAllWork.class, 3000, room.getRoomId());
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("总结算任务错误", e);
        }
    }

    @Override
    public void startGameSeatStatus(GameRoom room) {
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.isReady()) {
                seat.setGameCount(seat.getGameCount() + 1);
            }
        }
    }

    @Override
    public void solveRoom(int roomId) {
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room.getRoomType() == GameConfig.FREE_FIELD) {
            // 机器人个数
            for (Seat seat : room.getSeatMap().values()) {
                if (seat.getPlayer() == null) {
                    continue;
                }
                seat.getPlayer().setLeaveReason(LeaveReason.LEAVE_SOLVE);
                this.quitRoom(seat.getPlayer(), true);
            }
            room.clearAll();
        }
    }

    /**
     * 检查是否进入发牌阶段
     */
    @Override
    public void canEnterDeal(GameRoom room) {
        synchronized (room) {
            if (room.getSeatMap().size() < 2) {
                return;
            }
            if (room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY) {
                log.error("room:{} status is wrong", room.getRoomId());
                return;
            }
            if (room.readyCount() >= 2 && room.readyCount() == room.getSeatMap().size()) {
                int robotCount = 0;
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.getPlayer() instanceof Robot) {
                        robotCount += 1;
                        seat.getPlayer().setPlayRounds(seat.getPlayer().getPlayRounds() + 1);
                    }
                }
                if (room.getSeatMap().size() == robotCount) {
                    this.solveRoom(room.getRoomId());
                    return;
                }
                room.setGameNumber(this.idGeneratorService.getGameNum());
                room.setRoomStatus(RoomStatus.STATE_FA_PAI);
                room.setRound(room.getRound() + 1);
                this.startGameSeatStatus(room);
                this.enterDeal(room);
            }
        }
    }
}
