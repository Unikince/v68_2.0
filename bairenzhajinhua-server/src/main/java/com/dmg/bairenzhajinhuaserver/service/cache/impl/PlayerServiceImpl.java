package com.dmg.bairenzhajinhuaserver.service.cache.impl;

import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@Service
@ComponentScan(basePackages = {"com.dmg.data.client"})
public class PlayerServiceImpl implements PlayerService {
    @Value("${game-id}")
    private int gameId;
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
        player.setHeadIcon(user.getHeadImage());
        player.setSex(user.getSex());
        this.updatePlayer(player);
        return player;
    }

    public BetRecvDto bet(BetSendDto betSendDto) {
        betSendDto.setGameId(gameId);
        return this.nettySend.bet(betSendDto);
    }

    @Override
    public SettleRecvDto settle(SettleSendDto sendDto) {
        sendDto.setGameId(gameId);
        return this.nettySend.settle(sendDto);
    }

    /**
     * 同步房间
     */
    @Override
    public void syncRoom(int roomId, long userId, int roomLevel) {
        int gameId = this.gameId;
        SyncRoomSendDto sendDto = SyncRoomSendDto.builder()
                .roomLevel(roomLevel)
                .gameId(gameId)
                .roomId(roomId)
                .userId(userId).build();
        this.nettySend.syncRoom(sendDto);
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