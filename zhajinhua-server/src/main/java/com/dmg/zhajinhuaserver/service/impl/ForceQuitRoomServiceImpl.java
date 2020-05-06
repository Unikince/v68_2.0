package com.dmg.zhajinhuaserver.service.impl;


import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.model.constants.GameType;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.ForceQuitRoomService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_FORCE_LEAVEROOM;
import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_LEAVEROOMNTC;

/**
 * @Description 退出房间
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
@Service
@Slf4j
public class ForceQuitRoomServiceImpl implements ForceQuitRoomService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PushService pushService;

    @Autowired
    private PlayerService playerCacheService;

    @Override
    public void forceQuitRoom(Player player, Integer leave) {
        GameRoom room = roomService.getRoom(player.getRoomId());
        if (room == null) {
            pushService.push(player.getRoleId(), PLAYER_FORCE_LEAVEROOM, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD) {
            pushService.push(player.getRoleId(), PLAYER_FORCE_LEAVEROOM, ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        int seatId = roomService.getPlaySeat(room, player);
        Seat seat = roomService.getSeat(room, player);
        synchronized (room) {
            if (room.isReady() && seat.getHand().size() > 0) {
                pushService.push(player.getRoleId(), PLAYER_FORCE_LEAVEROOM, ResultEnum.PLAYER_HAS_PLAYING.getCode());
            } else {
                room.getSeatMap().remove(seatId);
                room.getWatchList().remove(player);
                Map<String, Object> map = new HashMap<>();
                map.put(D.PLAYER_RID, player.getRoleId());
                map.put(D.LEAVE_ROOM_MESSAGE_REASON, leave == null ? Config.LeaveReason.LEAVE_NORMAL : leave);
                MessageResult messageResult = new MessageResult(PLAYER_LEAVEROOMNTC, map);
                pushService.push(player.getRoleId(), messageResult);
                seat.getPlayer().setRoomId(0);
                playerCacheService.update(seat.getPlayer());
                // 通知其他玩家此玩家退出房间
                pushService.broadcast(messageResult, room);
                if (!(player instanceof Robot)) {
                    RoomManager.instance().getPlayerRoomIdMap().remove(player.getRoleId());
                }
            }
        }
    }
}
