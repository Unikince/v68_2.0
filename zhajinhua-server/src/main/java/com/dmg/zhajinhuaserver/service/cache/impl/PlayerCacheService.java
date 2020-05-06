package com.dmg.zhajinhuaserver.service.cache.impl;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dmg.zhajinhuaserver.common.constant.RegionConstant;
import com.dmg.zhajinhuaserver.model.bean.Player;

@Service
public class PlayerCacheService {

    @Cacheable(cacheNames = RegionConstant.PLAYER, key = "#userId", unless = "#result == null")
    public Player getPlayer(Long userId) {
        return null;
    }

    @CachePut(cacheNames = RegionConstant.PLAYER, key = "#player.roleId")
    public Player update(Player player) {
        return player;
    }
}