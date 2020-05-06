package com.dmg.niuniuserver.platform.service;

import java.math.BigDecimal;
import java.util.List;

import com.dmg.data.common.dto.*;
import com.dmg.niuniuserver.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.dmg.data.client.NettySend;
import com.dmg.niuniuserver.model.bean.Player;

/**
 * 玩家信息
 */
@Service
@ComponentScan(basePackages = {"com.dmg.data.client"})
public class PlayerService {

    @Autowired
    private NettySend nettySend;

    @Autowired
    private PlayerCacheService playerCacheService;

    /**
     * 获取玩家信息(从平台重新读取并更新到redis缓存),用于每次登陆刷新缓存
     *
     * @param playerId 玩家id
     * @return 玩家信息
     */
    public Player getPlayerPlatform(long playerId) {
        UserRecvDto user = this.nettySend.getUser(UserSendDto.builder()
                .userId(playerId).build());
        if (user == null) {
            return null;
        }
        Player player = this.getPlayer(playerId);
        if (player == null) {
            player = new Player();
            player.setUserId(playerId);
        }
        player.setNickname(user.getNickname());
        player.setGold(user.getGold().doubleValue());
        this.update(player);
        return player;
    }

    /**
     * 获取玩家信息
     *
     * @param playerId 玩家id
     * @return 玩家信息
     */
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
        player.setUserId(playerId);
        player.setNickname(user.getNickname());
        player.setGold(user.getGold().doubleValue());
        player.setHeadImg(user.getHeadImage());
        player.setSex(user.getSex());
        this.update(player);
        return player;
    }

    /**
     * 更新玩家信息
     *
     * @param player 玩家信息
     * @return 玩家信息
     */
    public Player update(Player player) {
        return this.playerCacheService.update(player);
    }

    public void decreaseGold(BigDecimal changeGold, long playerId) {
        this.nettySend.bet(BetSendDto.builder()
                .decGold(changeGold)
                .gameId(Constant.GAME_ID)
                .userId(playerId).build());
    }

    public void increaseGold(BigDecimal changeGold, long playerId) {
        this.nettySend.settle(SettleSendDto.builder()
                .changeGold(changeGold)
                .userId(playerId)
                .gameId(Constant.GAME_ID).build());
    }

    /**
     * 同步房间
     */
    public void syncRoom(int roomId, long userId) {
        this.nettySend.syncRoom(SyncRoomSendDto.builder()
                .gameId(Constant.GAME_ID)
                .roomId(roomId)
                .userId(userId).build());
        Player player = this.getPlayer(userId);
        if (player != null) {
            player.setRoomId(roomId);
            this.update(player);
        }
    }
}