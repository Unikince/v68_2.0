package com.dmg.fish.business.platform.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.SyncRoomSendDto;
import com.dmg.data.common.dto.UserRecvDto;
import com.dmg.data.common.dto.UserSendDto;
import com.dmg.fish.business.platform.model.Player;
import com.dmg.fish.business.platform.model.Robot;

import cn.hutool.core.util.RandomUtil;

/**
 * 玩家信息
 */
@Service
public class PlayerService {
    public static long ROBOT_ID_PREFIX = 900000000;

    @Value("${game-id}")
    private int gameId;
    @Autowired
    private NettySend nettySend;
    @Autowired
    private PlayerCacheService playerCacheService;

    /**
     * 获取玩家信息
     *
     * @param playerId 玩家id
     * @return 玩家信息
     */
    public Player getPlayerPlatform(long playerId) {
        UserSendDto sendDto = UserSendDto.builder().build();
        sendDto.setUserId(playerId);
        UserRecvDto user = this.nettySend.getUser(sendDto);
        if (user.getCode() != 0) {
            return null;
        }
        Player player = this.getPlayer(playerId);
        if (player == null) {
            player = new Player();
            player.setId(playerId);
        }
        player.setNickname(user.getNickname());
        player.setHeadImg(user.getHeadImage());
        player.setSex(user.getSex());
        player.setGold((long) (user.getGold().doubleValue() * 100));
        this.update(player);

        return player;
    }

    /**
     * 获取玩家信息
     *
     * @param playerId 玩家id
     * @return 玩家信息
     */
    @Cacheable(cacheNames = "fish_player", key = "#playerId", unless = "#result == null")
    public Player getPlayer(long playerId) {
        Player player = this.playerCacheService.getPlayer(playerId);
        if (player != null) {
            return player;
        }
        UserRecvDto user = this.nettySend.getUser(UserSendDto.builder()
                .userId(playerId).build());
        if (user == null) {
            return null;
        }
        player = new Player();
        player.setId(playerId);
        player.setNickname(user.getNickname());
        player.setHeadImg(user.getHeadImage());
        player.setSex(user.getSex());
        player.setGold((long) (user.getGold().doubleValue() * 100));
        this.update(player);
        return player;
    }

    /**
     * 更新玩家信息
     *
     * @param Player 玩家信息
     * @return 玩家信息
     */
    @CachePut(cacheNames = "fish_player", key = "#player.id")
    public Player update(Player player) {
        return this.playerCacheService.update(player);
    }

    /**
     * 结算
     */
    public void settle(long userId, BigDecimal changeGold) {
        SettleSendDto sendDto = SettleSendDto.builder().gameId(this.gameId).userId(userId).changeGold(changeGold).build();
        this.nettySend.settle(sendDto);
    }

    /**
     * 同步房间
     */
    public void syncRoom(int roomLevem, int roomId, long userId) {
        int gameId = this.gameId;
        SyncRoomSendDto sendDto = SyncRoomSendDto.builder().build();
        sendDto.setGameId(gameId);
        sendDto.setRoomLevel(roomLevem);
        sendDto.setRoomId(roomId);
        sendDto.setUserId(userId);
        this.nettySend.syncRoom(sendDto);
    }

    /**
     * 获取机器人信息
     *
     * @param playerId 玩家id
     * @return 机器人信息
     */
    public Robot getRobot(long playerId) {
        return this.playerCacheService.getRobot(playerId);
    }

    /**
     * 获取机器人信息，生成金币值
     *
     * @param playerId 玩家id
     * @param minGold 最小金币
     * @param maxGold 最大金币
     * @return 机器人信息
     */
    public Robot getRobot(long playerId, long minGold, long maxGold) {
        Robot obj = this.getRobot(playerId);
        if (obj != null) {
            obj.setGold(RandomUtil.randomLong(minGold, maxGold));
            this.update(obj);
        }
        return obj;
    }
}