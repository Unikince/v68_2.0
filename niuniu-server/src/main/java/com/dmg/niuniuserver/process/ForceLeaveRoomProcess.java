package com.dmg.niuniuserver.process;

import java.util.HashMap;
import java.util.Map;

import com.dmg.niuniuserver.constant.Constant;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.model.bean.Robot;
import com.dmg.niuniuserver.model.bean.Seat;
import com.dmg.niuniuserver.model.constants.LeaveReason;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 强制离开房间
 * @Author mice
 * @Date 2019/7/2 13:49
 * @Version V1.0
 **/
@Service
@Slf4j
public class ForceLeaveRoomProcess implements AbstractMessageHandler {
    @Autowired
    private PushService pushService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private PlayerService playerService;

    @Override
    public String getMessageId() {
        return MessageConfig.FORCE_LEAVE_ROOM;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = this.playerService.getPlayer(userId);
        if (this.forceQuitRoom(player) && !(player instanceof Robot)) {
            RoomManager.instance().getPlayerRoomIdMap().remove(userId);
        }
        this.playerService.update(player);
    }

    public Boolean forceQuitRoom(Player player) {
        GameRoom room = RoomManager.instance().getRoom(player.getRoomId());
        if (room == null) {
            this.pushService.push(player.getUserId(), MessageConfig.FORCE_LEAVE_ROOM, ResultEnum.PLAYER_HAS_NOT_INROOM.getCode());
            return true;
        }
        int seatId = this.roomService.getPlayerSeatId(room, player);
        Seat seat = this.getSeat(room, player);
        if (seat == null) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", player.getUserId());
            map.put("leaveReason", LeaveReason.LEAVE_NORMAL);
            this.pushService.push(player.getUserId(), MessageConfig.FORCE_LEAVE_ROOM);
            room.setPlayerNumber(room.getSeatMap().size());
            // 通知其他玩家此玩家退出房间
            MessageResult messageResult = new MessageResult(MessageConfig.PLAYER_LEAVE_ROOM_NTC, map);
            this.pushService.broadcast(messageResult, room);
            this.playerService.syncRoom(0, player.getUserId());
            return true;
        }
        // 牛牛准备后不能退出房间
        if (seat.isReady() || (seat.isReady() && seat.getGameCount() > 0)) {
            this.pushService.push(player.getUserId(), MessageConfig.FORCE_LEAVE_ROOM, ResultEnum.PLAYER_HAS_PLAYING.getCode());
            return false;
        } else {
            room.getSeatMap().remove(seatId);
            Map<String, Object> map = new HashMap<>();
            map.put("userId", player.getUserId());
            map.put("leaveReason", LeaveReason.LEAVE_NORMAL);
            this.pushService.push(player.getUserId(), MessageConfig.FORCE_LEAVE_ROOM);
            room.setPlayerNumber(room.getSeatMap().size());
            // 通知其他玩家此玩家退出房间
            MessageResult messageResult = new MessageResult(MessageConfig.PLAYER_LEAVE_ROOM_NTC, map);
            this.pushService.broadcast(messageResult, room);
            this.playerService.syncRoom(0, player.getUserId());
            if (!(player instanceof Robot)) {
                GameOnlineChangeUtils.decOnlineNum(Constant.GAME_ID, room.getLevel());
            }
            return true;
        }
    }

    /**
     * 获取玩家座位号
     *
     * @param room
     * @param player
     * @return
     */
    private Seat getSeat(GameRoom room, Player player) {
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer().getUserId().intValue() == player.getUserId().intValue()) {
                return seat;
            }
        }
        return null;
    }
}