package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.manager.TimerManager;
import com.dmg.zhajinhuaserver.manager.work.delay.ReadyTimeEndGameStartDelayWork;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.ReadyService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.StartGameService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYERREADY;
import static com.dmg.zhajinhuaserver.config.MessageConfig.SEND_READY_NTC;

/**
 * @author mice
 * @description: 准备
 * @return
 * @date 2019/7/10
 */
@Service
@Slf4j
public class ReadyServiceImpl implements ReadyService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PushService pushService;

    @Override
    public void ready(Player player, boolean ready) {
        log.info("准备:{},玩家id:{}", ready, player.getRoleId());
        GameRoom room = RoomManager.instance().getRoom(player.getRoomId());
        if (room == null) {
            pushService.push(player.getRoleId(), PLAYERREADY, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        if (room.getGameRoomTypeId() == D.FREE_FIELD) {
            if (room.getRoomStatus() != Config.RoomStatus.READY && room.getRoomStatus() != Config.RoomStatus.WAIT) {
                pushService.push(player.getRoleId(), PLAYERREADY, ResultEnum.ROOM_HAS_STARTED.getCode());
                return;
            }
        }
        int seat = roomService.getPlaySeat(room, player);
        Seat seatData = roomService.getSeat(room, player);
        if (seat == 0) {
            // 不在座位上
            pushService.push(player.getRoleId(), PLAYERREADY, ResultEnum.PLAYER_HAS_NO_SEAT.getCode());
            return;
        }
        if (player.getGold() < room.getLowerLimit().doubleValue()) {
            pushService.push(player.getRoleId(), PLAYERREADY, ResultEnum.PLAYER_HAS_NO_MONEY.getCode());
            return;
        }
        synchronized (room) {
            seatData.setReady(ready);
            seatData.setPass(false);
            seatData.setActionEndTime(0);
            seatData.setState(Config.SeatState.STATE_WAIT_STAR);
            Map<String, Object> map = new HashMap<>();
            map.put(D.PLAYER_RID, player.getRoleId());
            map.put(D.TABLE_ACTION_SEAT_INDEX, seat);
            map.put(D.TABLE_STATE, room.getRoomStatus());
            MessageResult messageResult = new MessageResult(SEND_READY_NTC, map);
            pushService.broadcast(messageResult, room);
            if (room.getGameRoomTypeId() == D.FREE_FIELD) {
                // 玩家都准备好，开始发牌
                this.checkAllReady(room);
            }
        }
    }

    /**
     * @param room
     * @return void
     * @description: 检查玩家是否全部准备
     * @author mice
     * @date 2019/7/13
     */
    public void checkAllReady(GameRoom room) {
        if (room.getSeatMap().size() < 2) {
            return;
        }
        int readyPlayer = 0;
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.isReady()) {
                readyPlayer += 1;
            }
        }
        if (!room.isReady() && readyPlayer == room.getSeatMap().size()) {
            try {
                TimerManager.instance().submitDelayWork(ReadyTimeEndGameStartDelayWork.class, 4000, room.getRoomId());
            } catch (SchedulerException e) {
                e.printStackTrace();
                log.error("submit ReadyTimeEndGameStartDelayWork error");
            }
        }
    }
}
