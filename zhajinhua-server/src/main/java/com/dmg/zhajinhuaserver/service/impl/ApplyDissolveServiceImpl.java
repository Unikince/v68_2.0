package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.ApplyDissolveService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.SUCCESS_DISOLUT_ROOM_NTC;

/**
 * @Description 玩家申请解散房间
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
@Service
public class ApplyDissolveServiceImpl implements ApplyDissolveService {
    @Autowired
    RoomService roomService;
    @Autowired
    PushService pushService;
    @Autowired
    PlayerService playerCacheService;

    @Override
    public void applyDissolveRoom(Player player) {
        GameRoom room = roomService.getRoom(player.getRoomId());
        if (room == null) {
            pushService.push( player.getRoleId(), MessageConfig.DISOLUT_ROOM, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        int seat = roomService.getPlaySeat(room, player);
        if (seat == 0) {
            pushService.push(player.getRoleId(),MessageConfig.DISOLUT_ROOM,ResultEnum.PLAYER_HAS_NO_SEAT.getCode());
            return;
        }
        synchronized (room) {
            if (!room.getDissolveMap().isEmpty()) {
                return;
            }
            pushService.push(player.getRoleId(),MessageConfig.DISOLUT_ROOM);
            if (room.getGameRoomTypeId() == D.PRIVATE_FIELD && room.getRound() < 1) {
                if (room.getRoomStatus() < Config.RoomStatus.GAME) {
                    if (player.getRoleId() == room.getCreator()) {
                        Map<String, Object> ret = new HashMap<>();
                        ret.put("dissolve", true);
                        MessageResult messageResult = new MessageResult(SUCCESS_DISOLUT_ROOM_NTC,ret);
                        pushService.broadcast(messageResult,room);
                        // 清除数据
                        for (Seat data : room.getSeatMap().values()) {
                            data.getPlayer().setRoomId(0);
                            playerCacheService.update(data.getPlayer());
                            data.clear(true);
                        }
                        room.clearAll();
                        roomService.deletePrivateRoom(room);
                        return;
                    } else {
                        pushService.push(player.getRoleId(),MessageConfig.PLAYER_LEAVEROOM,ResultEnum.PLAYER_ASK_WRONG.getCode());
                        return;
                    }
                }
            } else {
                if (!room.getSeatMap().get(seat).isPlayed()) {
                    pushService.push(player.getRoleId(),MessageConfig.PLAYER_LEAVEROOM,ResultEnum.PLAYER_ASK_WRONG.getCode());
                    return;
                }
            }
            room.getDissolveMap().put(seat, true);
            room.setDissolveRid((int) player.getRoleId());
        }
    }
}