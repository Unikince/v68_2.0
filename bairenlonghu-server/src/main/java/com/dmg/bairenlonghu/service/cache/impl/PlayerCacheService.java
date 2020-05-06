package com.dmg.bairenlonghu.service.cache.impl;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.sysconfig.RegionConfig;

@Service
public class PlayerCacheService {
    @Cacheable(cacheNames = RegionConfig.PLAYER_REDIS_KEY, key = "#playerId", unless = "#result == null")
    public BasePlayer getPlayer(long playerId) {
        return null;
    }

    @CachePut(cacheNames = RegionConfig.PLAYER_REDIS_KEY, key = "#player.userId")
    public BasePlayer updatePlayer(BasePlayer player) {
        return player;
    }

}