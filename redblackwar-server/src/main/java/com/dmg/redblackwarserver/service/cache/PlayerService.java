package com.dmg.redblackwarserver.service.cache;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.BetSendDto;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.SyncRoomSendDto;
import com.dmg.data.common.dto.UserRecvDto;
import com.dmg.data.common.dto.UserSendDto;
import com.dmg.redblackwarserver.common.model.BasePlayer;
import com.dmg.redblackwarserver.service.cache.impl.PlayerCacheService;

@Service
public class PlayerService {
    @Value("${game-id}")
    private int gameId;
    @Autowired
    private NettySend nettySend;
    @Autowired
    private PlayerCacheService playerCacheService;

    public BasePlayer getPlayerPlatform(long playerId) {
        UserSendDto sendDto = UserSendDto.builder().build();
        sendDto.setUserId(playerId);
        UserRecvDto user = this.nettySend.getUser(sendDto);
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

    public void bet(long playerId, BigDecimal decGold) {
        BetSendDto sendDto = BetSendDto.builder().build();
        sendDto.setGameId(this.gameId);
        sendDto.setUserId(playerId);
        sendDto.setDecGold(decGold);
        this.nettySend.bet(sendDto);
    }

    public void settle(long playerId, BigDecimal changeGold) {
        SettleSendDto sendDto = SettleSendDto.builder().build();
        sendDto.setGameId(this.gameId);
        sendDto.setUserId(playerId);
        sendDto.setChangeGold(changeGold);
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
        player.setHeadIcon(user.getHeadImage());
        player.setSex(user.getSex());
        this.updatePlayer(player);
        return player;
    }

    public BasePlayer updatePlayer(BasePlayer player) {
        return this.playerCacheService.updatePlayer(player);
    }
}