package com.dmg.niuniuserver.service.action.impl;

import static com.dmg.niuniuserver.config.MessageConfig.NTC_SEND_READ_INFO;
import static com.dmg.niuniuserver.config.MessageConfig.SITDOWN;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.dmg.niuniuserver.constant.Constant;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.manager.TimerManager;
import com.dmg.niuniuserver.manager.work.ReadyPlayerDelayWork;
import com.dmg.niuniuserver.manager.work.ReadyPlayerKickOutWork;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.model.dto.RoomInfoDTO;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;
import com.dmg.niuniuserver.service.action.JoinRoomService;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 加入房间
 * @Author mice
 * @Date 2019/7/2 18:53
 * @Version V1.0
 **/
@Service
@Slf4j
public class JoinRoomServiceImpl implements JoinRoomService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PushService pushService;

    @Autowired
    private PlayerService playerService;

    @Override
    public synchronized void joinRoom(Player player, int roomId) {
        log.info("玩家{},进入房间{}", player.getUserId(), roomId);
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            this.playerService.syncRoom(0, player.getUserId());
            this.pushService.push(player.getUserId(), MessageConfig.JOIN_ROOM, ResultEnum.ROOM_HAS_NO_EXIST.getCode());
            return;
        }
        synchronized (room) {
            // 判断重连
            if (this.checkRejoinRoom(player, room)) {
                log.info("玩家{},断线重连进入房间{}", player.getUserId(), roomId);
                this.rejionRoom(player, room);
                return;
            } else {
                player.setRoomId(0);
                this.playerService.update(player);
            }
        }
        // 如果是金币场，处理掉线的玩家
        room.getSeatMap().values().forEach(seat -> {
            if (!(seat.getPlayer() instanceof Robot)) {
                Player seatPlayer = this.playerService.getPlayer(seat.getPlayer().getUserId());
                if (!seatPlayer.isOnline()) {
                    this.roomService.quitRoom(seatPlayer, false);
                }
            }
        });
        room = RoomManager.instance().getRoom(roomId);
        // 如果该房间是自由场,并且该玩家携带的金币不足, 则禁止加入房间
        if (player.getGold() < room.getLowerLimit().doubleValue()) {
            this.pushService.push(player.getUserId(), MessageConfig.JOIN_ROOM, ResultEnum.PLAYER_HAS_NO_MONEY.getCode(), null);
            return;
        }
        // 加入房间
        Seat seatInfo = this.initSelfSeat(room, player);
        if (seatInfo == null) {
            return;
        }
        if (!(player instanceof Robot)) {
            this.playerService.syncRoom(roomId, player.getUserId());
            GameOnlineChangeUtils.incOnlineNum(Constant.GAME_ID, room.getLevel());
        }
        // 进入房间以后推送房内,当前所有玩家信息
        RoomInfoDTO tableInfo = this.roomService.getRoomInfo(room, null);
        Map<String, Object> actionInfo = new HashMap<>();
        if (room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY) {
            actionInfo.put("countdownTime", room.getPhaseCountdown());
            actionInfo.put("actionType", this.roomService.getActionType(room, null));
        }
        tableInfo.setActionInfo(actionInfo);
        Map<String, Object> map = new ConcurrentHashMap<>();
        map.put("tableInfo", tableInfo);
        Map<String, Object> resmap = new HashMap<>();
        Map<String, Object> seatMap = new HashMap<>();
        // 座位信息
        seatMap.put("seatInfo", this.roomService.getSeatInfo(room, seatInfo.getSeatId()));
        // 当前座位玩家信息
        seatMap.put("curSeatPlayerInfo", player);
        resmap.put("seatMsg", seatMap);
        map.put("sitDown", resmap);
        this.pushService.push(player.getUserId(), MessageConfig.JOIN_ROOM, ResultEnum.SUCCESS.getCode(), map);
        MessageResult messageResult = new MessageResult(MessageConfig.SITDOWNNTC, resmap);
        this.pushService.broadcastWithOutPlayer(messageResult, player, room);
        if (room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY) {
            return;
        }
        // 通知客户端当前玩家准备
        if (!seatInfo.isReady()) {
            Long timeMillis = System.currentTimeMillis();
            map = new HashMap<>();
            map.put("userId", player.getUserId());
            map.put("countdownTime", room.isPrivateRoom() ? 0 : timeMillis + room.getReadyTime());
            MessageResult countdownTimeMsgResult = new MessageResult(NTC_SEND_READ_INFO, map);
            this.pushService.push(player.getUserId(), countdownTimeMsgResult);
            // 获取时间戳
            String joinRoomTimeStamp = String.valueOf(timeMillis);
            seatInfo.getPlayer().setJoinRoomTimeStamp(joinRoomTimeStamp);
            try {
                if (!(player instanceof Robot)) {
                    TimerManager.instance().submitDelayWork(ReadyPlayerKickOutWork.class, room.getReadyTime(), room.getRoomId(), player.getUserId(), joinRoomTimeStamp);
                }
            } catch (SchedulerException e) {
                log.error("submitDelayWork ReadyPlayerKickOutWork error:{}", e);
            }
        }

        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer() instanceof Robot) {
                try {
                    int time = RandomUtil.randomInt(room.getReadyTime());
                    TimerManager.instance().submitDelayWork(ReadyPlayerDelayWork.class, time, room.getRoomId(), seat.getSeatId());
                } catch (SchedulerException e) {
                    log.error("submit ReadyPlayerDelayWork error:{}", e);
                }
            }
        }
    }

    /**
     * @Author liubo
     * @Description //TODO 初始化座位信息
     * @Date 17:34 2019/10/17
     **/
    private Seat initSelfSeat(GameRoom room, Player player) {
        // 找到一个空的坐位
        int lastSeatIndex = 0;
        Seat seatInfo = null;
        synchronized (room) {
            if (room.getSeatMap().size() == room.getTotalPlayer()) {
                room.getWatchList().remove(player);
                this.playerService.syncRoom(0, player.getUserId());
                this.pushService.push(player.getUserId(), SITDOWN, ResultEnum.ROOM_NO_SEAT.getCode());
                return seatInfo;
            }
            for (int i = 1; i <= room.getTotalPlayer(); i++) {
                if (room.getSeatMap().get(i) == null) {
                    lastSeatIndex = i;
                    break;
                }
            }
            if (lastSeatIndex <= 0 || lastSeatIndex > room.getTotalPlayer()) {
                this.pushService.push(player.getUserId(), MessageConfig.NTC_SEND_READ_INFO, ResultEnum.ROOM_NO_SEAT.getCode());
                return seatInfo;
            }
            player.setRoomId(room.getRoomId());
            seatInfo = new Seat(lastSeatIndex, player);
            if (room.getWinScoreMap().get(player.getUserId()) != null) {
                seatInfo.setScore(room.getWinScoreMap().get(player.getUserId()).getScore());
            }
            room.getSeatMap().put(lastSeatIndex, seatInfo);
            RoomManager.instance().getPlayerRoomIdMap().put(player.getUserId(), room.getRoomId());
            this.playerService.update(player);
            if (room.isPrivateRoom()) {
                seatInfo.setChipsRemain(0);
                // 如果是固定庄家的玩法,那么庄家需要有带入分
                if (room.getCustomRule().getGamePlay() == 5 && seatInfo.getPlayer().getUserId() == room.getCreator()) {
                    seatInfo.setChipsRemain(room.getCustomRule().getCarryFraction());
                }
            }
            if (room.getSeatMap().size() == 2) {
                if (!room.isPrivateRoom() || (room.isPrivateRoom() && room.getCustomRule().isAutoStart())) {
                    room.setRoomStatus(RoomStatus.STATE_WAIT_ALL_READY);
                    room.setPhaseCountdown(room.getReadyTime());
                }
            }
            // 如果是私人场,并且是固定庄家的玩法,那么直接定庄
            if (room.fixedZhuangJia() && seatInfo.getPlayer().getUserId() == room.getCreator()) {
                log.debug("定庄3,房间号为{}", room.getRoomId());
                this.roomService.definitionBanker(room, seatInfo);
            }
        }
        return seatInfo;
    }

    /**
     * 判断是否是断线重连
     */
    private boolean checkRejoinRoom(Player player, GameRoom room) {
        if (room == null || room.getSeatMap() == null) {
            return false;
        }
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer().getUserId().intValue() == player.getUserId().intValue()) {
                return true;
            }
        }
        log.info("用户:{}不为[断线重连]", player.getUserId());
        return false;
    }

    /**
     * 断线重连
     *
     * @param player
     */
    private void rejionRoom(Player player, GameRoom room) {
        if (room == null) {
            this.playerService.syncRoom(0, player.getUserId());
            this.pushService.push(player.getUserId(), MessageConfig.JOIN_ROOM, ResultEnum.PLAYER_HAS_NOT_INROOM.getCode(), null);
            return;
        }
        player.setOnline(true);
        int seat = this.roomService.getPlayerSeatId(room, player);
        if (seat == 0) { // 不在座位上
            player.setRoomId(0);
            this.playerService.update(player);
            this.pushService.push(player.getUserId(), MessageConfig.JOIN_ROOM, ResultEnum.PLAYER_HAS_NOT_SEAT.getCode(), null);
        } else {
            Seat data = room.getSeatMap().get(seat);
            log.info("断线重连Seat:{}", data.toString());
            data.setOffline(false);
            data.getPlayer().setOnline(true);
            RoomInfoDTO tableInfo = this.roomService.getRoomInfo(room, data);
            Map<String, Object> actionInfo = new HashMap<>();
            // 倒计时
            actionInfo.put("countdownTime", room.getPhaseCountdown());
            if (room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY) {
                // 动作类型
                actionInfo.put("actionType", this.roomService.getActionType(room, data));
                // 判断房间是否发牌
                if (room.getRoomStatus() > RoomStatus.STATE_FA_PAI) {
                    if (data.isHaveSeenCard()) {
                        // 手牌
                        actionInfo.put("handCards", data.send(2));
                        // 牌型
                        actionInfo.put("handCardType", data.getHandCardsType().getValue());
                    } else {
                        actionInfo.put("handCards", data.send(1));
                    }
                }
                // 如果房间还没有庄家,说明房间状态最多到定庄
                if (room.getBankerSeat() == null) {
                    if (room.getRoomStatus() == RoomStatus.STATE_QIANG_ZHUANG) {
                        // 抢庄倍数
                        actionInfo.put("qiangZhuangMultiple", room.betMultiple);
                    }
                } else if (room.getBankerSeat() != null && data.getSeatId() != room.getBankerSeat().getSeatId()) {
                    actionInfo.put("bankerFlag", room.getBankerSeat() != null && room.getBankerSeat().getSeatId() == data.getSeatId());
                    if (room.getRoomStatus() == RoomStatus.STATE_XIAZHU) {
                        // 下注倍数
                        actionInfo.put("betMultiple", room.getDiscardMul(data));
                    }
                }
            }
            tableInfo.setActionInfo(actionInfo);
            Map<String, Object> tableinMap = new HashMap<>();
            tableinMap.put("tableInfo", tableInfo);
            this.pushService.push(player.getUserId(), MessageConfig.JOIN_ROOM, 1, tableinMap);
            Map<String, Object> notifyMap = new HashMap<>();
            notifyMap.put("playerOnline", player.isOnline());
            notifyMap.put("userId", player.getUserId());
            notifyMap.put("seatId", seat);
            MessageResult messageResult = new MessageResult(1, notifyMap, MessageConfig.PLAYER_IS_ONLINE);
            this.pushService.broadcastWithOutPlayer(messageResult, player, room);
            room.getSeatMap().get(seat).setOffline(false);
            GameOnlineChangeUtils.incOnlineNum(Constant.GAME_ID, room.getLevel());
        }
    }
}