package com.dmg.zhajinhuaserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.QuitRoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_LEAVEROOM;

/**
 * @author mice
 * @description: 退出房间
 * @return
 * @date 2019/7/10
 */
@Service
public class QuitRoomProcess implements AbstractMessageHandler {

    @Autowired
    private PlayerService playerCacheService;

    @Autowired
    private QuitRoomService quitRoomService;

    @Override
    public String getMessageId() {
        return PLAYER_LEAVEROOM;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        quitRoomService.quitRoom(player, false, Config.LeaveReason.LEAVE_NORMAL);
    }
}
