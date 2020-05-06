package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.ReadyService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYERREADY;

/**
 * @Description  玩家准备
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
@Service
@Slf4j
public class ReadyProcess implements  AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return PLAYERREADY;
    }
    @Autowired
    PlayerService playerCacheService ;
    @Autowired
    ReadyService readyService ;

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        readyService.ready(player, true);

    }
}
