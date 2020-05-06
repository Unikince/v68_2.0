package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.service.PushService;
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
        return MessageConfig.HEART_BIT;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        pushService.push(userId,MessageConfig.HEART_BIT);
    }
}