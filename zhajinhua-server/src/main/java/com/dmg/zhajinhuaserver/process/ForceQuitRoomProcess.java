package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.ForceQuitRoomService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_FORCE_LEAVEROOM;

/**
 * @Description 退出房间
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
@Service
public class ForceQuitRoomProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return PLAYER_FORCE_LEAVEROOM;
    }

    @Autowired
    RoomService roomService;
    @Autowired
    PlayerService playerCacheService;
    @Autowired
    ForceQuitRoomService forceQuitRoomService;

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        forceQuitRoomService.forceQuitRoom(player,null);
    }
}
