package com.dmg.bairenlonghu.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenlonghu.service.logic.JoinRoomService;
import com.dmg.bairenlonghu.tcp.server.AbstractMessageHandler;
import com.dmg.bairenlonghu.tcp.server.MessageIdConfig;
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
    private JoinRoomService joinRoomService;

    @Override
    public String getMessageId() {
        return MessageIdConfig.JOIN_ROOM;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        int roomLevel = params.getInteger("roomLevel");
        joinRoomService.playerJoinRoom(userId,roomLevel);
    }
}