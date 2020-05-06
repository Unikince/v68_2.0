package com.dmg.clubserver.process.TableManage;


import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.config.GameType;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dmg.clubserver.config.MessageConfig.GAME_CONFIG;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/4 9:32
 * @Version V1.0
 **/
@Service
public class GameConfigProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return GAME_CONFIG;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Map<Integer, Map<Integer,Integer>> gameConfig = new ConcurrentHashMap<>();
        for (GameType gameType : GameType.values()) {
            Map<Integer,Integer> roomCardCost = new HashMap<>();
            roomCardCost.put(4,2);
            roomCardCost.put(8,3);
            roomCardCost.put(16,5);
            gameConfig.put(gameType.getKey(),roomCardCost);
        }
        result.setMsg(JSONObject.toJSON(gameConfig));
    }
}