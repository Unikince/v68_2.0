package com.dmg.bairenzhajinhuaserver.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.model.dto.LoginDTO;
import com.dmg.bairenzhajinhuaserver.service.PushService;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.tcp.server.AbstractMessageHandler;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;

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
        if (null == player) {
            pushService.push(userId, MessageResult.error(MessageIdConfig.LOGIN, ResultEnum.ACCOUNT_NOT_EXIST));
            return;
        }
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPlayer(player);
        loginDTO.setGoldLimitMap(RoomManager.intance().getFileLimitMap());
        loginDTO.setBankerLimitMap(RoomManager.intance().getBankerLimitMap());
        loginDTO.setBetChipMap(RoomManager.intance().getBetChipMap());
        MessageResult messageResult = new MessageResult(MessageIdConfig.LOGIN, loginDTO);
        pushService.push(userId, messageResult);
    }
}