package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.service.JoinRoomService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.JION_ROOM;

/**
 * @Description 玩家进入房间
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
@Service
public class JoinRoomProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return JION_ROOM;
    }

    @Autowired
    private PlayerService playerCacheService;

    @Autowired
    private JoinRoomService joinRoomService;

    @Autowired
    private PushService pushService;

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        // 检查是否维护
        if (RoomManager.instance().isShutdownServer()) {
            MessageResult messageResult = new MessageResult(MessageConfig.SHUTDOWN_SERVER_NTC);
            pushService.push(userId, messageResult);
            return;
        }
        Player player = playerCacheService.getPlayer(userId);
        // 玩家断线时间清空
        player.setDisconnectTime(0);
        int roomId = params.getInteger("roomId");
        joinRoomService.joinRoom(player, roomId);
    }
}
