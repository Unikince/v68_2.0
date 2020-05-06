package com.dmg.niuniuserver.process;

import static com.dmg.niuniuserver.config.MessageConfig.PLAYER_LEAVE_ROOM_NTC;
import static com.dmg.niuniuserver.config.MessageConfig.QUICK_CHANGE_ROOM;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.manager.RoomManager;
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

/**
 * @Description
 * @Author mice
 * @Date 2019/7/2 14:27
 * @Version V1.0
 **/
@Service
public class QuickChangeRoomProcess implements AbstractMessageHandler {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PushService pushService;
    @Autowired
    private RoomService roomService;

    @Override
    public String getMessageId() {
        return QUICK_CHANGE_ROOM;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = this.playerService.getPlayer(userId);
        player.setLeaveReason(0);
        GameRoom room = RoomManager.instance().getRoom(player.getRoomId());
        if (room == null) {
            this.pushService.push(userId, QUICK_CHANGE_ROOM, ResultEnum.PLAYER_HAS_NOT_INROOM.getCode());
            return;
        }
        int seat = 0;
        for (Map.Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
            if (entry.getValue().getPlayer().getUserId().intValue() == player.getUserId().intValue()) {
                seat = entry.getKey();
            }
        }
        if (room.getRoomStatus() <= RoomStatus.STATE_WAIT_ALL_READY || (room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY && !room.getSeatMap().get(seat).isReady())) {
            room.getSeatMap().remove(seat);
            Map<String, Object> map = new HashMap<>();
            map.put("userId", player.getUserId());
            map.put("leaveReason", player.getLeaveReason());
            MessageResult messageResult = new MessageResult(PLAYER_LEAVE_ROOM_NTC, map);
            this.pushService.broadcastWithOutPlayer(messageResult, player, room);
        } else {
            this.pushService.push(userId, QUICK_CHANGE_ROOM, ResultEnum.PLAYER_HAS_PLAYING.getCode());
            return;
        }
        if (player.getDisconnectTime() > 0) {
            player.setDisconnectTime(0);
        }
        int roomId = 0;
        int grade = room.getLevel();
        // 判断房间剩余玩家类型
        {
            // 房间是否全是机器人
            boolean allrobot = true;
            for (Seat checkSeat : room.getSeatMap().values()) {
                if (checkSeat.getPlayer() == null) {
                    continue;
                } else if (checkSeat.getPlayer() instanceof Robot) {
                    continue;
                } else {
                    allrobot = false;
                }
            }
            if (allrobot) {
                // 全是机器人,解散房间
                this.roomService.solveRoom(roomId);
            }
        }
        // 加入新房间
        Map<Integer, GameRoom> map = RoomManager.instance().getRoomMap();
        for (GameRoom gameRoom : map.values()) {
            if (gameRoom.getLevel() != grade) {
                continue;
            }
            if (gameRoom.getSeatMap().size() < gameRoom.getTotalPlayer() && gameRoom.getRoomId() != room.getRoomId()) {
                roomId = gameRoom.getRoomId();
                break;
            }
        }
        if (roomId == 0) {
            this.pushService.push(userId, QUICK_CHANGE_ROOM, ResultEnum.ROOM_HAS_EMPTY.getCode());
        } else {
            GameRoom room2 = RoomManager.instance().getRoom(roomId);
            if (room2.getRoomStatus() == 0) {
                room2.setRoomStatus(RoomStatus.STATE_WAIT_ALL_READY);
            }
            Map<String, Integer> resultMap = new HashMap<>();
            resultMap.put("roomId", roomId);
            MessageResult messageResult = new MessageResult(QUICK_CHANGE_ROOM, resultMap);
            this.pushService.push(userId, messageResult);
        }
    }

}