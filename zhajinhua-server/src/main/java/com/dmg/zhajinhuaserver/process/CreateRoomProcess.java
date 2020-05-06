package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.CreateRoomService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.CREATE_PRIVATE_ROOM;

/**
 * @Description 创建房间
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
@Service
public class CreateRoomProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return CREATE_PRIVATE_ROOM;
    }
@Autowired
RoomService roomService ;
    @Autowired
    PlayerService playerCacheService;
    @Autowired
    CreateRoomService createRoomService;
    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        Map<String, Object> str = params.getInnerMap();
        createRoomService.createRoom(player, str);
    }
}
