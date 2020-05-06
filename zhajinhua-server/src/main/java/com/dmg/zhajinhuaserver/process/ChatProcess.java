package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.ChatService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.GAME_CHAT_MSG;


/**
 * @Description 玩家互动
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/

@Service
@Slf4j
public class ChatProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return GAME_CHAT_MSG;
    }

    @Autowired
    RoomService roomService;
    @Autowired
    PlayerService playerCacheService;
    @Autowired
    ChatService chatService;

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        chatService.chat(player, params);
    }
}
