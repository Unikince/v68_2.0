package com.dmg.zhajinhuaserver.process;
import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.SitDownService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.SITDOWN;

/**
 * @description: 坐下
 * @return
 * @author mice
 * @date 2019/7/10
*/
@Service
@Slf4j
public class SitdownProcess implements AbstractMessageHandler {
    @Autowired
    private SitDownService sitDownService;
    @Override
    public String getMessageId() {
        return SITDOWN;
    }
    @Autowired
    RoomService roomService  ;
    @Autowired
    PlayerService playerCacheService  ;

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        int seat = params.getInteger(D.TABLE_ACTION_SEAT_INDEX);
        sitDownService.sitDown(player, seat);
    }
}
