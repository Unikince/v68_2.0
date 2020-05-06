package com.dmg.niuniuserver.process;

import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.service.action.JoinRoomService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 加入房间
 * @Author mice
 * @Date 2019/7/1 18:30
 * @Version V1.0
 **/
@Service
@Slf4j
public class JoinRoomProcess implements AbstractMessageHandler {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private JoinRoomService joinRoomService;

    @Autowired
    private PushService pushService;

    @Override
    public String getMessageId() {
        return MessageConfig.JOIN_ROOM;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        // 检查是否维护
        if (RoomManager.instance().isShutdownServer()) {
            MessageResult messageResult = new MessageResult(MessageConfig.SHUTDOWN_SERVER_NTC);
            this.pushService.push(userId, messageResult);
            return;
        }
        Player player = this.playerService.getPlayer(userId);
        Integer roomId = params.getInteger("roomId");
        // 玩家断线时间清空
        player.setDisconnectTime(0);
        this.joinRoomService.joinRoom(player, roomId);
        this.playerService.update(player);
    }
}