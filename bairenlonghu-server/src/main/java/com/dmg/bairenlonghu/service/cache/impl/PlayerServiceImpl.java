package com.dmg.bairenlonghu.service.cache.impl;


import com.dmg.bairenlonghu.common.constant.Constant;
import com.dmg.data.common.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.service.cache.PlayerService;
import com.dmg.data.client.NettySend;

@Service
@ComponentScan(basePackages = { "com.dmg.data.client" })
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private NettySend nettySend;

    @Autowired
    private PlayerCacheService playerCacheService;

    @Override
    public int getRoomLevel(long playerId) {
        UserRecvDto user = this.nettySend.getUser(UserSendDto.builder().userId(playerId).build());
        if (user == null) {
            return 0;
        }
        return user.getRoomLevel();
    }

    @Override
    public BasePlayer getPlayerPlatform(long playerId) {
        UserRecvDto user = this.nettySend.getUser(UserSendDto.builder().userId(playerId).build());
        if (user == null) {
            return null;
        }
        BasePlayer player = this.getPlayer(playerId);
        if (player == null) {
            player = new BasePlayer();
            player.setUserId((int) playerId);
        }
        player.setNickname(user.getNickname());
        player.setGold(user.getGold());
        this.updatePlayer(player);
        return player;
    }

    public BetRecvDto bet(BetSendDto betSendDto) {
        betSendDto.setGameId(Constant.GAME_ID);
        return this.nettySend.bet(betSendDto);
    }

    @Override
    public SettleRecvDto settle(SettleSendDto sendDto) {
        sendDto.setGameId(Constant.GAME_ID);
        return this.nettySend.settle(sendDto);
    }

    /**
     * 同步房间
     */
    @Override
    public void syncRoom(int roomId, long userId, int roomLevel) {
        int gameId = Constant.GAME_ID;
        SyncRoomSendDto syncRoomSendDto = SyncRoomSendDto.builder()
                .gameId(gameId)
                .userId(userId)
                .roomId(roomId)
                .roomLevel(roomLevel).build();
        this.nettySend.syncRoom(syncRoomSendDto);
    }

    @Override
    public BasePlayer getPlayer(long playerId) {
        BasePlayer player = this.playerCacheService.getPlayer(playerId);
        if (player != null) {
            return player;
        }
        UserRecvDto user = this.nettySend.getUser(UserSendDto.builder()
                .userId(playerId).build());
        if (user == null) {
            return null;
        }
        player = new BasePlayer();
        player.setUserId((int) playerId);
        player.setNickname(user.getNickname());
        player.setGold(user.getGold());
        this.updatePlayer(player);
        return player;
    }

    @Override
    public BasePlayer updatePlayer(BasePlayer player) {
        return this.playerCacheService.updatePlayer(player);
    }

}