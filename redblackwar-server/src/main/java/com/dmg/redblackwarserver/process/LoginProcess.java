package com.dmg.redblackwarserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.redblackwarserver.common.model.BasePlayer;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.manager.RoomManager;
import com.dmg.redblackwarserver.model.Player;
import com.dmg.redblackwarserver.model.dto.LoginDTO;
import com.dmg.redblackwarserver.service.PushService;
import com.dmg.redblackwarserver.service.cache.PlayerService;
import com.dmg.redblackwarserver.tcp.server.AbstractMessageHandler;
import com.dmg.redblackwarserver.tcp.server.MessageIdConfig;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 登录游戏大厅
 * @Author mice
 * @Date 2019/18/30 16:00
 * @Version V1.0
 **/
@Service
public class LoginProcess implements AbstractMessageHandler {

    @Autowired
    private PlayerService playerCacheService;

    @Autowired
    private PushService pushService;

    @Override
    public String getMessageId() {
        return MessageIdConfig.LOGIN;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        // 检查是否维护
        if (RoomManager.intance().isShutdownServer()) {
            MessageResult messageResult = new MessageResult(MessageIdConfig.SHUTDOWN_SERVER_NTC);
            this.pushService.push(userId, messageResult);
            return;
        }
        BasePlayer player = playerCacheService.getPlayer(userId);
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPlayer(player);
//        Map<String,Integer> limitMap = new HashMap<>();
//        RoomManager.intance().getGoldLimitMap().keySet().forEach(key -> {
//        	limitMap.put(key, 0);
//        });
        loginDTO.setBetChipMap(RoomManager.intance().getBetChipsMap2());
        loginDTO.setGoldLimitMap(RoomManager.intance().getGoldLimitMap());
        MessageResult messageResult = new MessageResult(MessageIdConfig.LOGIN, loginDTO);
        player.push(messageResult);
    }
}