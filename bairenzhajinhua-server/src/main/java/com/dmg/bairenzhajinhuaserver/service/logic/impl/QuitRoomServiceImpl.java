package com.dmg.bairenzhajinhuaserver.service.logic.impl;

import com.dmg.server.common.util.GameOnlineChangeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.model.BaseRobot;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.model.Seat;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.service.logic.QuitRoomService;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;

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

    @Override
    public void quitRoom(int userId, boolean kick) {
        BasePlayer basePlayer = this.playerCacheService.getPlayer(userId);
        if (basePlayer == null) {
            return;
        }
        if (basePlayer.getRoomId() <= 0) {
            basePlayer.push(MessageIdConfig.QUIT_ROOM);
            return;
        }

        Room room = RoomManager.intance().getRoom(basePlayer.getRoomId());
        Seat seat;
        synchronized (room) {
            if (room.getBanker().getPlayer().getUserId() == basePlayer.getUserId()) {
                // seat = room.getBanker();
                basePlayer.push(MessageIdConfig.QUIT_ROOM, ResultEnum.BANKER_PLAYER_QUIT_WRONG.getCode());
                return;
            } else {
                seat = RoomManager.intance().getSeat(room, basePlayer.getUserId());
            }
            if (seat == null) {
                basePlayer.setRoomId(0);
                if (!(basePlayer instanceof BaseRobot)) {
                    this.playerCacheService.syncRoom(0, basePlayer.getUserId(),room.getLevel());
                }
                basePlayer.setSeatIndex(0);
                this.playerCacheService.updatePlayer(basePlayer);
                basePlayer.push(MessageIdConfig.QUIT_ROOM);
                return;
            }
            if (seat.getUserBetChipTotal() == null || seat.getUserBetChipTotal().isEmpty()) {
                if (room.getApplyToZhuangPlayerList().contains(basePlayer)) {
                    room.getApplyToZhuangPlayerList().remove(basePlayer);
                }
                room.getSeatMap().remove(String.valueOf(seat.getSeatIndex()));
                seat.setApplyBanker(false);
                seat.setNoBetCount(0);
            }
        }
        // 游戏中可退出
        seat.setLeave(true);
        basePlayer.setRoomId(0);
        if (!(basePlayer instanceof BaseRobot)) {
            if (seat == null || (seat.isBanker() == false && seat.getUserBetChipTotal().isEmpty())) {
                this.playerCacheService.syncRoom(0, basePlayer.getUserId(),room.getLevel());
            }
        }

        basePlayer.setSeatIndex(0);
        this.playerCacheService.updatePlayer(basePlayer);
        if (kick) {
            basePlayer.push(new MessageResult(MessageIdConfig.KICK_QUIT_ROOM, new Object()));
        } else {
            basePlayer.push(MessageIdConfig.QUIT_ROOM);
        }
        GameOnlineChangeUtils.decOnlineNum(Integer.parseInt(RoomManager.intance().getGameId()),room.getLevel());
    }
}