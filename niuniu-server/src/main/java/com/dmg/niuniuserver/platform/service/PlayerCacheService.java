package com.dmg.niuniuserver.platform.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.platform.model.PlatfromConfig;

/**
 * 玩家信息
 */
@Service
public class PlayerCacheService {

    /**
     * 获取玩家信息
     *
     * @param playerId 玩家id
     * @return 玩家信息
     */
    @Cacheable(cacheNames = PlatfromConfig.PLAYER_REDIS_KEY, key = "#playerId", unless = "#result == null")
    public Player getPlayer(long playerId) {
        return null;
    }

    /**
     * 更新玩家信息
     *
     * @param Player 玩家信息
     * @return 玩家信息
     */
    @CachePut(cacheNames = PlatfromConfig.PLAYER_REDIS_KEY, key = "#player.id")
    public Player update(Player player) {
        return player;
    }
}