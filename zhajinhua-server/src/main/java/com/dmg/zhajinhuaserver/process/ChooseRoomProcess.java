package com.dmg.zhajinhuaserver.process;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.MessageConfig;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;

/**
 * @Description 选择房间
 * @Author jock
 * @Date 2019/7/3 13:57
 * @Version V1.0
 **/
@Service
public class ChooseRoomProcess implements AbstractMessageHandler {
    @Autowired
    private PlayerService playerCacheService;
    @Autowired
    private PushService pushService;

    @Override
    public String getMessageId() {
        return MessageConfig.CHOOSE_ROOM;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = this.playerCacheService.getPlayerPlatform(userId);
        if (player == null) {
            this.pushService.push(userId, MessageConfig.CHOOSE_ROOM, ResultEnum.GET_USERINFO_FAIL.getCode());
            return;
        }
        Map map = RoomManager.instance().getPlayerRoomIdMap();
        if (null != map.get(userId)) {
            MessageResult messageResult = new MessageResult(MessageConfig.CHOOSE_ROOM, map.get(userId));
            this.pushService.push(userId, messageResult);
            return;
        }
        Integer roomLevel = params.getInteger("roomLevel");
        GameRoom gameRoom = RoomManager.instance().getGameRoomId(roomLevel, player.getGold());
        if (gameRoom != null) {
            MessageResult messageResult = new MessageResult(MessageConfig.CHOOSE_ROOM, gameRoom.getRoomId());
            this.pushService.push(userId, messageResult);
        } else {
            this.pushService.push(userId, MessageConfig.CHOOSE_ROOM, ResultEnum.ROOM_HAS_EMPTY.getCode());
        }
    }
}