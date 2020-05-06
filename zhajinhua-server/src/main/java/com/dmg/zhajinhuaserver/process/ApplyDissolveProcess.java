package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.ApplyDissolveService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.DISBAND_ROOM;


/**
 * @Description  申请解散房间
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
@Service
public class ApplyDissolveProcess  implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return DISBAND_ROOM;
    }
    @Autowired
    PlayerService playerCacheService;
    @Autowired
    RoomService roomService ;
    @Autowired
    ApplyDissolveService applyDissolveService;
    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        applyDissolveService.applyDissolveRoom(player);
    }
}
