package com.dmg.niuniuserver.service.action.impl;

import static com.dmg.niuniuserver.config.MessageConfig.NTC_SEND_READ_INFO;
import static com.dmg.niuniuserver.config.MessageConfig.SITDOWN;

import java.util.HashMap;
import java.util.Map;

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
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;
import com.dmg.niuniuserver.service.action.SitDownService;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/4 14:50
 * @Version V1.0
 **/
@Service
@Slf4j
public class SitDownServiceImpl implements SitDownService {

    @Autowired
    private PushService pushService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private PlayerService playerService;

    @Override
    public synchronized void sitDown(Player player) {
        int seatId = 0;
        GameRoom room = RoomManager.instance().getRoom(player.getRoomId());
        if (room.getSeatMap().size() == room.getTotalPlayer()) {
            room.getWatchList().remove(player);
            this.playerService.syncRoom(0, player.getUserId());
            this.pushService.push(player.getUserId(), SITDOWN, ResultEnum.ROOM_NO_SEAT.getCode());
            return;
        }
        // 找到一个空的坐位
        for (int i = 1; i <= room.getTotalPlayer(); i++) {
            if (room.getSeatMap().get(i) == null) {
                seatId = i;
                break;
            }
        }

        // 坐到找到的那个空位
        if (seatId > 0 && seatId <= room.getTotalPlayer()) {
            // 从围观者列表删除
            room.getWatchList().remove(player);
            // 获取时间戳
            String joinRoomTimeStamp = String.valueOf(System.currentTimeMillis());
            player.setJoinRoomTimeStamp(joinRoomTimeStamp);
            Seat seatInfo = new Seat(seatId, player);
            if (room.getWinScoreMap().get(player.getUserId()) != null) {
                seatInfo.setScore(room.getWinScoreMap().get(player.getUserId()).getScore());
            }
            room.getSeatMap().put(seatId, seatInfo);

            if (room.isPrivateRoom()) {
                seatInfo.setChipsRemain(0);
                // 如果是固定庄家的玩法,那么庄家需要有带入分
                if (room.getCustomRule().getGamePlay() == 5 && seatInfo.getPlayer().getUserId() == room.getCreator()) {
                    seatInfo.setChipsRemain(room.getCustomRule().getCarryFraction());
                }
            }
            if (!room.isPrivateRoom() || (room.isPrivateRoom() && room.getCustomRule().isAutoStart())) {
                try {
                    if (!(player instanceof Robot)) {
                        TimerManager.instance().submitDelayWork(ReadyPlayerKickOutWork.class, room.getReadyTime(), room.getRoomId(), player.getUserId(), joinRoomTimeStamp);
                    }
                } catch (SchedulerException e) {
                    log.error("submitDelayWork ReadyPlayerKickOutWork error:{}", e);
                }
            }
            if (room.getSeatMap().size() == 2) {
                if (!room.isPrivateRoom() || (room.isPrivateRoom() && room.getCustomRule().isAutoStart())) {
                    room.setRoomStatus(RoomStatus.STATE_WAIT_ALL_READY);
                    room.setPhaseCountdown(room.getReadyTime());
                }
            }
            this.pushService.push(player.getUserId(), SITDOWN);
            Map<String, Object> resmap = new HashMap<>();
            Map<String, Object> map = new HashMap<>();
            // 座位信息
            map.put("seatInfo", this.roomService.getSeatInfo(room, seatId));
            // 当前座位玩家信息
            map.put("curSeatPlayerInfo", player);
            resmap.put("seatMsg", map);
            MessageResult messageResult = new MessageResult(MessageConfig.SITDOWNNTC, resmap);
            this.pushService.broadcast(messageResult, room);
            // 如果是私人场,并且是固定庄家的玩法,那么直接定庄
            if (room.fixedZhuangJia() && seatInfo.getPlayer().getUserId() == room.getCreator()) {
                log.debug("定庄3,房间号为{}", room.getRoomId());
                this.roomService.definitionBanker(room, seatInfo);
            }
            if (room.getRoomStatus() <= RoomStatus.STATE_WAIT_ALL_READY) {
                // 通知客户端当前玩家准备
                if (!seatInfo.isReady()) {
                    map = new HashMap<>();
                    map.put("userId", player.getUserId());
                    map.put("countdownTime", room.isPrivateRoom() ? 0 : System.currentTimeMillis() + room.getReadyTime());
                    MessageResult countdownTimeMsgResult = new MessageResult(NTC_SEND_READ_INFO, map);
                    this.pushService.push(player.getUserId(), countdownTimeMsgResult);
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
        } else {
            this.pushService.push(player.getUserId(), MessageConfig.NTC_SEND_READ_INFO, ResultEnum.ROOM_NO_SEAT.getCode());
        }
    }

}