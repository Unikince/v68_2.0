package com.dmg.bairenniuniuserver.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.bairenniuniuserver.common.result.MessageResult;
import com.dmg.bairenniuniuserver.manager.RoomManager;
import com.dmg.bairenniuniuserver.model.dto.LoginDTO;
import com.dmg.bairenniuniuserver.service.PushService;
import com.dmg.bairenniuniuserver.service.cache.PlayerService;
import com.dmg.bairenniuniuserver.tcp.server.AbstractMessageHandler;
import com.dmg.bairenniuniuserver.tcp.server.MessageIdConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Autowired

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
        BasePlayer player = this.playerCacheService.getPlayer(userId);
        player.setOnline(true);
        this.playerCacheService.updatePlayer(player);
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPlayer(player);
        loginDTO.setGoldLimitMap(RoomManager.intance().getFileLimitMap());
        loginDTO.setBankerLimitMap(RoomManager.intance().getBankerLimitMap());
        loginDTO.setBetChipMap(RoomManager.intance().getBetChipMap());
        String multipleConfig = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            multipleConfig = objectMapper.writeValueAsString(RoomManager.intance().getMultipleConfigMap());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        JSONObject multipleConfigJsonObject = JSON.parseObject(multipleConfig);
        loginDTO.setMultipleConfig(multipleConfigJsonObject);
        MessageResult messageResult = new MessageResult(MessageIdConfig.LOGIN, loginDTO);
        this.pushService.push(userId, messageResult);
    }
}