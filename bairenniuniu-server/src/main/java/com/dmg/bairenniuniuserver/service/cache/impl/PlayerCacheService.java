package com.dmg.bairenniuniuserver.service.cache.impl;

import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.bairenniuniuserver.service.PushService;
import com.dmg.bairenniuniuserver.sysconfig.RegionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class PlayerCacheService {
    @Autowired
    private PushService pushService;
    @Cacheable(cacheNames = RegionConfig.PLAYER_REDIS_KEY, key = "#playerId", unless = "#result == null")
    public BasePlayer getPlayer(long playerId) {
        return null;
    }

    @CachePut(cacheNames = RegionConfig.PLAYER_REDIS_KEY, key = "#player.userId")
    public BasePlayer updatePlayer(BasePlayer player) {
        return player;
    }
}