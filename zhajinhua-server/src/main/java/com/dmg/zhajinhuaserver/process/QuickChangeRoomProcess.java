package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Room;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.JoinRoomService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_LEAVEROOMNTC;
import static com.dmg.zhajinhuaserver.config.MessageConfig.QUICK_CHANGE_ROOM;

/**
 * @description: 立即换房
 * @return
 * @author mice
 * @date 2019/7/10
*/
@Service
@Slf4j
public class QuickChangeRoomProcess implements AbstractMessageHandler {
    @Autowired
    private PushService pushService;
    @Autowired
    private JoinRoomService joinRoomService;
    @Override
    public String getMessageId() {
        return QUICK_CHANGE_ROOM;
    }

    @Autowired
    PlayerService playerCacheService;

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        if (System.currentTimeMillis() - player.getChangeRoomTime() < 1000) {
            pushService.push(userId,QUICK_CHANGE_ROOM, ResultEnum.PLAYER_ASK_TOO_FAST.getCode());
            return;
        }
        player.setChangeRoomTime(System.currentTimeMillis());
        player.setLeaveReason(0);
        GameRoom room = (GameRoom) RoomManager.instance().getRoom(player.getRoomId());
        if (room == null) {
            pushService.push(userId,QUICK_CHANGE_ROOM, ResultEnum.PLAYER_HAS_NOT_INROOM.getCode());
            return;
        }
        Seat seat = null;
        for (Seat entry : room.getSeatMap().values()) {
            if (entry.getPlayer().getRoleId() == player.getRoleId()) {
                seat = entry;
            }
        }
        if (!room.isReady() || (room.isReady() && !seat.isReady())) {
            room.getSeatMap().remove(seat.getSeatId());
            room.getWatchList().remove(player);
            Map<String, Object> map = new HashMap<>();
            map.put(D.PLAYER_RID, player.getRoleId());
            map.put(D.LEAVE_ROOM_MESSAGE_REASON, player.getLeaveReason());
            MessageResult messageResult = new MessageResult(PLAYER_LEAVEROOMNTC,map);
            // 通知其他玩家此玩家退出房间
            pushService.broadcastWithOutPlayer(messageResult, player,room);
        } else {
            pushService.push(userId,QUICK_CHANGE_ROOM, ResultEnum.PLAYER_HAS_PLAYING.getCode());
            return;
        }

        if (player.getDisconnectTime() > 0) {
            player.setDisconnectTime(0);
        }
        int roomId = 0;
        Map<Integer, GameRoom> map = RoomManager.instance().getRoomMap();
        for (Room rooms : map.values()) {
            if (rooms.getGameTypeId() != room.getGameTypeId() || rooms.getGrade() != room.getGrade()) {
                continue;
            }
            GameRoom room1 = (GameRoom) rooms;
            if (room1.getSeatMap().size() < room1.getTotalPlayer() && room1.getRoomId() != room.getRoomId()) {
                roomId = room1.getRoomId();
                break;
            }
        }
        if (roomId == 0) {
            pushService.push(userId,QUICK_CHANGE_ROOM, ResultEnum.ROOM_NO_EXIST.getCode());
        } else {
            GameRoom room2 = (GameRoom) RoomManager.instance().getRoom(roomId);
            if (room2.getRoomStatus() == 0) {
                room2.setRoomStatus(Config.RoomStatus.READY);
            }
            pushService.push(userId,QUICK_CHANGE_ROOM);
            joinRoomService.joinRoom(player, roomId);
        }
    }
}
