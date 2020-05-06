package com.dmg.bairenzhajinhuaserver.service.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.service.logic.HeartService;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/6 14:32
 * @Version V1.0
 **/
@Service
public class HeartServiceImpl implements HeartService {

    @Autowired
    private PlayerService playerCacheService;

    @Override
    public void heart(int userId) {
        BasePlayer basePlayer = playerCacheService.getPlayer(userId);
        if (basePlayer == null){
            return;
        }
        basePlayer.push(MessageIdConfig.HEART_BIT);
    }
}