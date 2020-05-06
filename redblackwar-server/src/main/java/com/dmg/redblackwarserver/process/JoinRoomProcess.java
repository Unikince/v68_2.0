package com.dmg.redblackwarserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.manager.RoomManager;
import com.dmg.redblackwarserver.service.PushService;
import com.dmg.redblackwarserver.service.logic.JoinRoomService;
import com.dmg.redblackwarserver.tcp.server.AbstractMessageHandler;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 加入房间
 * @Author mice
 * @Date 2019/7/31 16:05
 * @Version V1.0
 **/
@Service
public class JoinRoomProcess implements AbstractMessageHandler {

    @Autowired
    private PushService pushService;

    @Autowired
    private JoinRoomService joinRoomService;

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
        joinRoomService.playerJoinRoom(userId, roomLevel);
    }
}