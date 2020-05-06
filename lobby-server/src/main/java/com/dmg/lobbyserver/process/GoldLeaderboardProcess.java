package com.dmg.lobbyserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.config.MessageConfig;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 金币排行榜 前50
 * @Author mice
 * @Date 2019/12/24 17:45
 * @Version V1.0
 **/
@Service
public class GoldLeaderboardProcess implements AbstractMessageHandler{

    @Autowired
    private UserService userService;
    @Override
    public String getMessageId() {
        return MessageConfig.GOLDLEADERBOARD;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        result.setMsg(userService.getLeaderboard(50));
    }
}