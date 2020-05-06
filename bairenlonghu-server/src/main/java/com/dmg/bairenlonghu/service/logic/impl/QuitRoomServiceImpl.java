package com.dmg.bairenlonghu.service.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.common.model.BaseRobot;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.model.Seat;
import com.dmg.bairenlonghu.service.PushService;
import com.dmg.bairenlonghu.service.cache.PlayerService;
import com.dmg.bairenlonghu.service.logic.QuitRoomService;
import com.dmg.bairenlonghu.tcp.server.MessageIdConfig;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/6 14:32
 * @Version V1.0
 **/
@Service
public class QuitRoomServiceImpl implements QuitRoomService {
    @Autowired
    private PlayerService playerCacheService;
    @Autowired
    private PushService pushService;

    @Override
    public void quitRoom(int userId) {
        BasePlayer basePlayer = this.playerCacheService.getPlayer(userId);
        if (basePlayer == null) {
            return;
        }
        if (basePlayer.getRoomId() <= 0) {
            this.pushService.push(userId, MessageIdConfig.QUIT_ROOM);
            return;
        }

        Room room = RoomManager.intance().getRoom(basePlayer.getRoomId());
        Seat seat;
        if (room.getBanker().getPlayer().getUserId() == basePlayer.getUserId()) {
            seat = room.getBanker();
        } else {
            seat = RoomManager.intance().getSeat(room, basePlayer.getUserId());
        }
        if (seat != null) {
            seat.setLeave(true);
        }

        for (BasePlayer basePlayer1 : room.getApplyToZhuangPlayerList()) {
            if (basePlayer1.getUserId() == userId) {
                room.getApplyToZhuangPlayerList().remove(basePlayer1);
                break;
            }
        }
        basePlayer.setRoomId(0);
        if (!(basePlayer instanceof BaseRobot)) {
            if (seat == null || (seat.isBanker() == false && seat.getBetChipMap().isEmpty())) {
                this.playerCacheService.syncRoom(0, basePlayer.getUserId(), room.getLevel());
            }
        }
        basePlayer.setSeatIndex(0);
        this.playerCacheService.updatePlayer(basePlayer);
        this.pushService.push(userId, MessageIdConfig.QUIT_ROOM);
    }
}