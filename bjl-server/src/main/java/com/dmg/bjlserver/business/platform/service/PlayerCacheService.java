package com.dmg.bjlserver.business.platform.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.platform.model.Player;

@Component
public class PlayerCacheService {

    @Cacheable(cacheNames = "bjl:player", key = "#playerId", unless = "#result == null")
    public Player getPlayer(long playerId) {
        return null;
    }

    @CachePut(cacheNames = "bjl:player", key = "#player.id")
    public Player updatePlayer(Player player) {
        return player;
    }
}