package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.AnswerDissolveService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.ANSWER_DISOLUT_ROOM;

/**
 * @Description 玩家投票选择解散房间或者继续游戏
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
@Service
public class AnswerDissolveProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return ANSWER_DISOLUT_ROOM;
    }

    @Autowired
    RoomService roomService;
    @Autowired
    PlayerService playerCacheService;
    @Autowired
    AnswerDissolveService answerDissolveRoom;
    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        boolean agree = (boolean) params.getObject("data", Map.class).get("agree");
        answerDissolveRoom.answerDissolveRoom(player, agree);

    }
}
