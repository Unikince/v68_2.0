package com.dmg.bairenzhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenzhajinhuaserver.service.PushService;
import com.dmg.bairenzhajinhuaserver.tcp.server.AbstractMessageHandler;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 心跳
 * @Author mice
 * @Date 2019/7/31 18:03
 * @Version V1.0
 **/
@Service
public class HeartBeatProcess implements AbstractMessageHandler {
    @Autowired
    private PushService pushService;
    @Override
    public String getMessageId() {
        return MessageIdConfig.HEART_BIT;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        pushService.push(userId, MessageIdConfig.HEART_BIT);
    }
}