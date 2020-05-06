package com.dmg.niuniuserver.process;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.dmg.niuniuserver.service.PushService;

/**
 * @Description 选择房间
 * @Author mice
 * @Date 2019/7/3 13:57
 * @Version V1.0
 **/
@Service
public class ChooseRoomProcess implements AbstractMessageHandler {
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PushService pushService;

    @Override
    public String getMessageId() {
        return MessageConfig.CHOOSE_ROOM;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = this.playerService.getPlayerPlatform(userId);
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