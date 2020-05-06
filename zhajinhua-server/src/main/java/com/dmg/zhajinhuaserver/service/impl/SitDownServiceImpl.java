package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.manager.work.delay.QuitRoomDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.ReadyPlayerDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.ReadyTimeEndDelayWork;
import com.dmg.zhajinhuaserver.manager.work.delay.TimeEndDelayWork;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.constants.GameConfig;
import com.dmg.zhajinhuaserver.model.dto.OutPlayerDTO;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.ReadyService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.SitDownService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.*;

/**
 * @author mice
 * @description: 坐下
 * @return
 * @date 2019/7/10
 */
@Service
@Slf4j
public class SitDownServiceImpl implements SitDownService {

    @Autowired
    private PushService pushService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ReadyService readyService;

    @Autowired
    private PlayerService playerCacheService;

    @Override
    public void sitDown(Player player, int seatIndex) {
        GameRoom room = RoomManager.instance().getRoom(player.getRoomId());
        if (room == null) {
            pushService.push(player.getRoleId(), SITDOWN, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        Seat seat = null;
        if (seatIndex == D.DEFINENUM) {
            // 坐下
            seat = this.findEmptySeatSitDown(player, room);
        } else if (seatIndex == 0) {
            // 站起
            this.standUp(player, room);
        }
        if (room.getRoomStatus() == Config.RoomStatus.WAIT || room.getRoomStatus() == Config.RoomStatus.READY) {
            if (room.getGameRoomTypeId() == D.FREE_FIELD) {
                //机器人自动准备
                if (player instanceof Robot) {
                    readyService.ready(player, true);
                }
            } else {
                if (player.getRoleId() == room.getCreator()) {
                    readyService.ready(player, true);
                }
            }
            //未准备踢出
            if (seat != null) {
                //发送准备消息
                long time = System.currentTimeMillis() + (room.getReadyTime());
                Map<String, Object> map = new HashMap<>();
                map.put(D.READY_TO_TIME, time);
                MessageResult messageResult = new MessageResult(PLAYER_READY_NTC, map);
                pushService.push(seat.getPlayer().getRoleId(), messageResult);
                room.setRoomStatus(Config.RoomStatus.READY);
                OutPlayerDTO outPlayerDTO = new OutPlayerDTO();
                outPlayerDTO.setSeatId(seat.getSeatId());
                outPlayerDTO.setJoinRoomTimeStamp(seat.getPlayer().getJoinRoomTimeStamp());
                outPlayerDTO.setLeaveReason(Config.LeaveReason.LEAVE_NOREADY);
                try {
                    TimerManager.instance().submitDelayWork(TimeEndDelayWork.class, room.getReadyTime(), room.getRoomId(), outPlayerDTO);
                } catch (SchedulerException e) {
                    log.error("submitDelayWork TimeEndDelayWork error:{}", e);
                }
            }
        }

    }

    /**
     * @param gameRoom
     * @return int
     * @description: 找空位置
     * @author mice
     * @date 2019/7/13
     */
    private int getSeatIndex(GameRoom gameRoom) {
        int seatIndex = 0;
        for (int i = 1; i <= gameRoom.getTotalPlayer(); i++) {
            // 找一个空位坐下
            if (gameRoom.getSeatMap().get(i) == null) {
                seatIndex = i;
                break;
            }
        }
        return seatIndex;
    }

    /**
     * @param player
     * @param room
     * @return void
     * @description: 找空位置坐下
     * @author mice
     * @date 2019/7/13
     */
    private Seat findEmptySeatSitDown(Player player, GameRoom room) {
        int seatIndex = 0;
        Seat seatInfo = null;
        synchronized (room) {
            seatIndex = this.getSeatIndex(room);
            if (seatIndex == 0) {
                pushService.push(player.getRoleId(), SITDOWN, ResultEnum.ROOM_NO_SEAT.getCode());
                return seatInfo;
            } else {
                room.getWatchList().remove(player);
                player.setJoinRoomTimeStamp(String.valueOf(System.currentTimeMillis()));
                playerCacheService.update(player);
                seatInfo = new Seat(seatIndex, player);
                if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
                    seatInfo.setChipsRemain(0);
                    if (room.getCreator() == player.getRoleId()) {
                        room.setBankerSeat(seatInfo.getSeatId());
                        room.setLastBanker(seatInfo.getSeatId() - 1 <= 0 ? room.getTotalPlayer() : seatInfo.getSeatId() - 1);
                    }
                }
                seatInfo.setState(Config.SeatState.STATE_WAIT_READY);
                if (room.getWinScoreMap().get(player.getRoleId()) != null) {
                    seatInfo.setScore(room.getWinScoreMap().get(player.getRoleId()).getScore());
                }
                room.getSeatMap().put(seatIndex, seatInfo);
            }
        }
        pushService.push(player.getRoleId(), SITDOWN);
        Map<String, Object> map = new HashMap<>();
        map.put(D.SEAT_INFO, roomService.sendSeatMsg(room, seatIndex));
        map.put(D.PLAYER_SEAT_PLAYERINFO, roomService.sendPlayerMsg(player));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(D.SEAT_MESSAGE, map);
        MessageResult messageResult = new MessageResult(SITDOWNNTC, resultMap);
        pushService.broadcast(messageResult, room);
        return seatInfo;
    }

    /**
     * @param player
     * @param room
     * @return void
     * @description: 站起
     * @author mice
     * @date 2019/7/13
     */
    private void standUp(Player player, GameRoom room) {
        int seatIndex = roomService.getPlaySeat(room, player);
        if (seatIndex == 0) {
            // 不在座位上
            pushService.push(player.getRoleId(), SITDOWN);
        } else {
            Seat data = room.getSeatMap().get(seatIndex);
            room.getSeatMap().remove(seatIndex);
            room.getWinScoreMap().put(player.getRoleId(), data);
            room.getWatchList().add(player);
            pushService.push(player.getRoleId(), SITDOWN);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("seat", seatIndex);
            resultMap.put("sitDown", false);
            MessageResult messageResult = new MessageResult(SITDOWNNTC, resultMap);
            pushService.broadcastWithOutPlayer(messageResult, player, room);
        }
    }
}
