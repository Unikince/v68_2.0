package com.dmg.redblackwarserver.service.logic.impl;

import java.math.BigDecimal;

import com.dmg.server.common.util.GameOnlineChangeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.redblackwarserver.common.model.BasePlayer;
import com.dmg.redblackwarserver.common.model.BaseRobot;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.common.result.ResultEnum;
import com.dmg.redblackwarserver.manager.RoomManager;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.Seat;
import com.dmg.redblackwarserver.model.constants.D;
import com.dmg.redblackwarserver.service.cache.PlayerService;
import com.dmg.redblackwarserver.service.logic.QuitRoomService;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;

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
            seat = RoomManager.intance().getSeat(room, basePlayer.getUserId());
            if (seat == null) {
                basePlayer.setRoomId(0);
                if (!(basePlayer instanceof BaseRobot)) {
                    this.playerCacheService.syncRoom(0, 0, basePlayer.getUserId());
                }
                basePlayer.setSeatIndex(0);
                this.playerCacheService.updatePlayer(basePlayer);
                basePlayer.push(MessageIdConfig.QUIT_ROOM);
                return;
            }

            if (seat.getUserBetChipTotal() != null && !seat.getUserBetChipTotal().isEmpty()) {
                boolean flag = false;
                if (seat.getUserBetChipTotal().get(D.ONE) != null && !seat.getUserBetChipTotal().get(D.ONE).equals(BigDecimal.ZERO)) {
                    flag = true;
                }
                if (seat.getUserBetChipTotal().get(D.TWO) != null && !seat.getUserBetChipTotal().get(D.TWO).equals(BigDecimal.ZERO)) {
                    flag = true;
                }
                if (seat.getUserBetChipTotal().get(D.THREE) != null && !seat.getUserBetChipTotal().get(D.THREE).equals(BigDecimal.ZERO)) {
                    flag = true;
                }
                if (flag) {
                    basePlayer.push(MessageIdConfig.QUIT_ROOM, ResultEnum.PLAYER_ASK_WRONG.getCode());
                    return;
                }
            }
            if (room.getApplyToZhuangPlayerList().contains(basePlayer)) {
                room.getApplyToZhuangPlayerList().remove(basePlayer);
            }
            room.getSeatMap().remove(String.valueOf(seat.getSeatIndex()));
        }
        seat.setApplyBanker(false);
        seat.setLeave(true);
        seat.setNoBetCount(0);
        basePlayer.setRoomId(0);
        if (!(basePlayer instanceof BaseRobot)) {
            this.playerCacheService.syncRoom(0, 0, basePlayer.getUserId());
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