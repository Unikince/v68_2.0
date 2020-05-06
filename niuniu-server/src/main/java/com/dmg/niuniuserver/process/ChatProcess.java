package com.dmg.niuniuserver.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.service.RoomService;

/**
 * @Description 聊天
 * @Author mice
 * @Date 2019/7/4 20:10
 * @Version V1.0
 **/
@Service
public class ChatProcess implements AbstractMessageHandler {
    @Autowired
    private RoomService roomService;
    @Autowired
    private PlayerService playerService;

    @Override
    public String getMessageId() {
        return MessageConfig.CHAT;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        this.roomService.chat(this.playerService.getPlayer(userId), params);
    }
}