package com.dmg.zhajinhuaserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.service.ChatService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.GAME_CHAT_MSGNTC;

/**
 * @Description 玩家互动
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    RoomService roomService;
    @Autowired
    private PushService pushService;

    @Override
    public void chat(Player player, JSONObject message) {
        GameRoom room = roomService.getRoom(player.getRoomId());
        if (room == null) {
            return;
        }
        MessageResult messageResult = new MessageResult(GAME_CHAT_MSGNTC, message);
        pushService.broadcast(messageResult, room);
    }
}
