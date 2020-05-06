package com.dmg.bairenzhajinhuaserver.process;

import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.service.logic.JoinRoomService;
import com.dmg.bairenzhajinhuaserver.tcp.server.AbstractMessageHandler;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;

/**
 * @Description 加入房间
 * @Author mice
 * @Date 2019/7/31 16:05
 * @Version V1.0
 **/
@Service
public class JoinRoomProcess implements AbstractMessageHandler {

    @Autowired
    private JoinRoomService joinRoomService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PushService pushService;

    @Override
    public String getMessageId() {
        return MessageIdConfig.JOIN_ROOM;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        // 检查是否维护
        if (RoomManager.intance().isShutdownServer()) {
            MessageResult messageResult = new MessageResult(MessageIdConfig.SHUTDOWN_SERVER_NTC);
            this.pushService.push(userId, messageResult);
            return;
        }
        int roomLevel = params.getInteger("roomLevel");

        int tlevel = playerService.getRoomLevel(userId);
        if (tlevel != 0) {
            roomLevel = tlevel;
        }

        joinRoomService.playerJoinRoom(userId, roomLevel);
    }
}