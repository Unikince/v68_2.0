package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.StartGameService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.dmg.zhajinhuaserver.config.MessageConfig.REQUEST_START_GAME;

/**
 * @description: 开始游戏
 * @return
 * @author mice
 * @date 2019/7/10
*/
@Service
public class StartGameProcess  implements AbstractMessageHandler{
    @Override
    public String getMessageId() {
        return REQUEST_START_GAME;
    }
    @Autowired
    PlayerService playerCacheService ;
    @Autowired
    private StartGameService startGameService;
    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        startGameService.startGame(player,player.getRoomId());
    }
}
