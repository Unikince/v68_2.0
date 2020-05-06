package com.dmg.zhajinhuaserver.service.cache.impl;

import java.math.BigDecimal;

import com.dmg.data.common.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.dmg.data.client.NettySend;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/1 18:47
 * @Version V1.0
 **/
@Service
@ComponentScan(basePackages = {"com.dmg.data.client"})
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private NettySend nettySend;

    @Autowired
    private PlayerCacheService playerCacheService;

    @Override
    public Player getPlayerPlatform(long playerId) {
        UserRecvDto user = nettySend.getUser(UserSendDto.builder()
                .userId(playerId).build());
        if (user == null) {
            return null;
        }
        Player player = this.getPlayer(playerId);
        if (player == null) {
            player = new Player();
            player.setRoleId(playerId);
        }
        player.setNickname(user.getNickname());
        player.setGold(user.getGold().doubleValue());
        this.update(player);
        return player;
    }

    @Override
    public Player getPlayer(Long userId) {
        Player player = this.playerCacheService.getPlayer(userId);
        if (player != null) {
            return player;
        }
        UserRecvDto user = this.nettySend.getUser(UserSendDto.builder()
                .userId(userId).build());
        if (user == null) {
            return null;
        }
        player = new Player();
        player.setRoleId(userId);
        player.setNickname(user.getNickname());
        player.setGold(user.getGold().doubleValue());
        player.setHeadImgUrl(user.getHeadImage());
        player.setSex(user.getSex());
        this.update(player);
        return player;
    }

    @Override
    public Player update(Player player) {
        return playerCacheService.update(player);
    }

    public BetRecvDto decreaseGold(BigDecimal changeGold, long playerId) {
        return nettySend.bet(BetSendDto.builder()
                .gameId(Constant.GAME_ID)
                .userId(playerId)
                .decGold(changeGold).build());
    }

    @Override
    public void increaseGold(BigDecimal changeGold, long playerId) {
        nettySend.settle(SettleSendDto.builder()
                .gameId(Constant.GAME_ID)
                .userId(playerId)
                .changeGold(changeGold).build());
    }

    /**
     * 同步房间
     */
    @Override
    public void syncRoom(int roomId, long userId) {
        nettySend.syncRoom(SyncRoomSendDto.builder()
                .userId(userId)
                .roomId(roomId)
                .gameId(Constant.GAME_ID).build());
    }
}