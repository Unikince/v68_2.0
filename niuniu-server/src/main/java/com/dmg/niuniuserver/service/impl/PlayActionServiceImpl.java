package com.dmg.niuniuserver.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.manager.TimerManager;
import com.dmg.niuniuserver.manager.work.OpenPokerWork;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.*;
import com.dmg.niuniuserver.model.dto.KaiPaiDTO;
import com.dmg.niuniuserver.model.dto.QiangZhuangDTO;
import com.dmg.niuniuserver.model.dto.XiaZhuDTO;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.dmg.niuniuserver.service.PlayActionService;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/2 19:15
 * @Version V1.0
 **/
@Service
@Slf4j
public class PlayActionServiceImpl implements PlayActionService {
    @Autowired
    private PushService pushService;
    @Autowired
    private RoomService roomService;


    public void kaiPai(GameRoom gameRoom, Seat seat, String cmd) {
        log.info("==>房间{}的{}号座位开牌", gameRoom.getRoomId(), seat.getSeatId());
        gameRoom.getOpenCards().put(seat.getSeatId(), seat);
        seat.setStatus(SeatState.KAIPAI_OVER);
        seat.setHaveSeenCard(true);
        KaiPaiDTO kaiPaiDTO = new KaiPaiDTO();
        kaiPaiDTO.setUserId(seat.getPlayer().getUserId());
        kaiPaiDTO.setSeatId(seat.getSeatId());
        kaiPaiDTO.setHandCardType(seat.getHandCardsType().getValue());
        kaiPaiDTO.setAllCardTypeList(seat.getWholeTenCards());
        kaiPaiDTO.setScatteredCardList(seat.getScatteredCards());
        if (!seat.getWholeTenCards().isEmpty()) {
            seat.getHand().clear();
            seat.getHand().addAll(seat.getWholeTenCards());
            seat.getHand().addAll(seat.getScatteredCards());
        }
        kaiPaiDTO.setHandCardList(seat.getHand());
        kaiPaiDTO.setActionType(ActionType.TYPE_KAIPAI);
        if (cmd != null) {
            MessageResult messageResult = new MessageResult(cmd, kaiPaiDTO);
            pushService.push(seat.getPlayer().getUserId(), messageResult);
        }
        MessageResult messageResult = new MessageResult(MessageConfig.DO_ACTION_RESULT_NTC, kaiPaiDTO);
        pushService.broadcast(messageResult, gameRoom);
        // 判断是否可以进行结算
        int openCards = gameRoom.getOpenCards().size();
        if (gameRoom.getplayerReadyCount() == openCards) {
            //进行结算
            log.info("房间:{},进入结算阶段", gameRoom.getRoomId());
            roomService.settlement(gameRoom.getRoomId());
        } else {
            log.info("房间：{}，进入结算阶段,参与本局人数:{},开牌人数：{}", gameRoom.getRoomId(), gameRoom.getplayerReadyCount(), openCards);
        }
    }

    public void xiaZhu(Seat seat, int multiple, GameRoom gameRoom) {
        log.info("==>房间{},{}号座位下注", gameRoom.getRoomId(), seat.getSeatId());
        if (gameRoom.getRoomStatus() != RoomStatus.STATE_XIAZHU) {
            log.error("==>房间{},不在下注阶段", gameRoom.getRoomId());
            return;
        }
        // 重复下注错误
        if (seat.getBetChips() > 0) {
            pushService.push(seat.getPlayer().getUserId(), MessageConfig.DO_ACTION, ResultEnum.REPEAT_BET.getCode());
            log.error("==>gameRoom,玩家{}重复下注", gameRoom.getRoomId(), seat.getPlayer().getUserId());
            return;
        }
        // 判断是否可以下注成功 押=玩家身上金币/底分/牌型最大倍率/抢的倍率
        if (seat.getChipsRemain() / gameRoom.getBaseScore().doubleValue() / gameRoom.getMul(Combination.WU_XIAO_CATTLE.getValue()) / gameRoom.getBankerMultiple() >= 1) {
            // 推送下注成功消息
            seat.setStatus(SeatState.XIAZHU_OVER);
            seat.setBetChips(multiple);
            XiaZhuDTO xiaZhuDTO = new XiaZhuDTO();
            xiaZhuDTO.setSeatId(seat.getSeatId());
            xiaZhuDTO.setActionType(ActionType.TYPE_XIAZHU);
            xiaZhuDTO.setMultiple(multiple);
            xiaZhuDTO.setUserId(seat.getPlayer().getUserId());
            pushService.push(seat.getPlayer().getUserId(), MessageConfig.DO_ACTION, ResultEnum.SUCCESS.getCode());
            MessageResult messageResult = new MessageResult(MessageConfig.DO_ACTION_RESULT_NTC, xiaZhuDTO);
            pushService.broadcast(messageResult, gameRoom);
            // 如果所有人下注完成,那么将所有牌推送出去
            try {
                long times = System.currentTimeMillis() + GameConfig.KAI_PAI_LENGTH_TIME;
                if (gameRoom.getBetPlayerCount() == gameRoom.getplayerReadyCount() - 1) {
                    // 提前虚拟结算
                    //roomService.virtualSettlement(gameRoom);
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("countdownTime", times);
                    maps.put("actionType", ActionType.TYPE_KAIPAI);
                    MessageResult message = new MessageResult(MessageConfig.TURN_ACTION, maps);
                    pushService.push(seat.getPlayer().getUserId(), message);
                    List<Seat> seatList = CollectionUtil.newCopyOnWriteArrayList(gameRoom.getSeatMap().values());
                    for (Seat seat1 : gameRoom.getSeatMap().values()) {
                        // 推送所有牌的数据
                        if (CollectionUtil.isNotEmpty(seat1.getHand())) {
                            roomService.pushDealMSG(seatList, seat1, 2);
                            seat1.setStatus(SeatState.KAIPAI);
                        } /*else {
                            Map<String, Object> map3 = new HashMap<>();
                            map3.put("userId", seat1.getPlayer().getUserId());
                            // 发牌次数
                            map3.put("dealCount", 2);
                            // 推送旁观者消息
                            pushService.push(seat1.getPlayer().getUserId(), MessageConfig.GAME_SEND_CARDS, ResultEnum.SUCCESS.getCode(), map3);
                        }*/
                    }
                    for (Seat s : gameRoom.getSeatMap().values()) {
                        if (s.isReady() && s.getGameCount() > 0) {
                            pushService.push(seat.getPlayer().getUserId(), message);
                        }
                    }
                    // 开牌任务启动
                    gameRoom.setPhaseCountdown(times);
                    gameRoom.setRoomStatus(RoomStatus.STATE_KAIPAI);
                    log.info("房间:{},开牌任务启动", gameRoom.getRoomId());
                    for (Seat data : gameRoom.getSeatMap().values()) {
                        if (data.getPlayer() instanceof Robot) {
                            int time = RandomUtil.randomInt(GameConfig.KAI_PAI_LENGTH_TIME);
                            TimerManager.instance().submitDelayWork(OpenPokerWork.class, time, gameRoom.getRoomId(), data.getSeatId());
                        }
                    }
                    TimerManager.instance().submitDelayWork(OpenPokerWork.class, GameConfig.KAI_PAI_LENGTH_TIME, gameRoom.getRoomId(), 0);
                }
            } catch (SchedulerException e) {
                log.error("submit OpenPokerWork error:{}", e);
            }
        } else {
            pushService.push(seat.getPlayer().getUserId(), MessageConfig.DO_ACTION, ResultEnum.PLAYER_HAS_NO_MONEY.getCode());
        }
    }

    /**
     * 抢庄操作
     */
    @Override
    public void qiangZhuang(Seat seat, int multiple) {
        GameRoom gameRoom = RoomManager.instance().getRoom(seat.getPlayer().getRoomId());
        log.info("房间{},座位{},进入抢庄环节", gameRoom.getRoomId(), seat.getSeatId());
        if (gameRoom.getRoomStatus() != RoomStatus.STATE_QIANG_ZHUANG) {
            pushService.push(seat.getPlayer().getUserId(), MessageConfig.DO_ACTION, ResultEnum.NO_PERMISSIONS.getCode());
            log.error("房间{}不在抢庄状态,操作用户为{}", gameRoom.getRoomId(), seat.getPlayer().getUserId());
            return;
        }
        if (seat != null && seat.isReady()) {
            int count = 0;
            for (Seat seatCount : gameRoom.getSeatMap().values()) {
                if (seatCount.isReady()) {
                    count++;
                }
            }
            //抢=玩家身上金币/底分/在场人数/牌型最大倍率/押的最大倍率
            if (seat.getChipsRemain() / gameRoom.getBaseScore().doubleValue()
                    / count
                    / gameRoom.getMul(Combination.WU_XIAO_CATTLE.getValue())
                    / gameRoom.getBetMultiple()[gameRoom.getBetMultiple().length - 1] < 1) {
                multiple = 0;
            }
            if (multiple > gameRoom.getCurMaxRobBet()) {
                gameRoom.setCurMaxRobBet(multiple);
            }
            if (gameRoom.getBankerFraction().get(seat.getSeatId()) == null) {
                gameRoom.getBankerFraction().put(seat.getSeatId(), multiple);
                seat.setStatus(SeatState.QIANGZHUANG_OVER);
            } else {
                return;
            }

            QiangZhuangDTO qiangZhuangDTO = new QiangZhuangDTO();
            qiangZhuangDTO.setSeatId(seat.getSeatId());
            qiangZhuangDTO.setActionType(ActionType.TYPE_QIANGZHUANG);
            qiangZhuangDTO.setMultiple(multiple);
            qiangZhuangDTO.setUserId(seat.getPlayer().getUserId());
            MessageResult messageResult = new MessageResult(MessageConfig.DO_ACTION_RESULT_NTC, qiangZhuangDTO);
            pushService.broadcast(messageResult, gameRoom);
            pushService.push(seat.getPlayer().getUserId(), MessageConfig.DO_ACTION, ResultEnum.SUCCESS.getCode());
        }
        // 如果可以进入定庄
        if (gameRoom.getplayerReadyCount() == gameRoom.getBankerFraction().size() && gameRoom.getRoomStatus() == RoomStatus.STATE_QIANG_ZHUANG) {
            log.info("房间{},进入定庄1环节", gameRoom.getRoomId());
            roomService.definitionBanker(gameRoom, null);
        }
    }
}