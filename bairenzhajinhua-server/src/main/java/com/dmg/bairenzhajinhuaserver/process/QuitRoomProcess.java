package com.dmg.bairenzhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenzhajinhuaserver.service.logic.QuitRoomService;
import com.dmg.bairenzhajinhuaserver.tcp.server.AbstractMessageHandler;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 退出房间
 * @Author mice
 * @Date 2019/8/6 14:25
 * @Version V1.0
 **/
@Service
public class QuitRoomProcess implements AbstractMessageHandler {
    @Autowired
    private QuitRoomService quitRoomService;
    @Override
    public String getMessageId() {
        return MessageIdConfig.QUIT_ROOM;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        quitRoomService.quitRoom(userId,false);
    }
}