package com.dmg.bjlserver.business.platform.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dmg.bjlserver.business.platform.model.Player;
import com.dmg.bjlserver.business.robot.RobotMgr;
import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.SyncRoomSendDto;
import com.dmg.data.common.dto.UserRecvDto;
import com.dmg.data.common.dto.UserSendDto;

import cn.hutool.core.util.RandomUtil;

@Component
public class PlayerService {
    @Value("${game-id}")
    private int gameId;
    @Autowired
    private NettySend nettySend;
    @Autowired
    private PlayerCacheService playerCacheService;

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
        player.setNickName(user.getNickname());
        player.setHeadImg(user.getHeadImage());
        player.setSex(user.getSex());
        player.setGold((long) (user.getGold().doubleValue() * 100));
        player.setHeadImg(user.getHeadImage());
        this.updatePlayer(player);
        return player;

    }

    public void settle(long playerId, long changeGold) {
        SettleSendDto sendDto = SettleSendDto.builder().build();
        sendDto.setGameId(this.gameId);
        sendDto.setUserId(playerId);
        sendDto.setChangeGold(new BigDecimal(changeGold).divide(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
        this.nettySend.settleAsync(sendDto);
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
        this.nettySend.syncRoomAsync(sendDto);
    }

    public Player getPlayer(long playerId) {
        return this.playerCacheService.getPlayer(playerId);
    }

    public Player updatePlayer(Player player) {
        return this.playerCacheService.updatePlayer(player);
    }

    public Map<Long, Player> acquireRobotPlayers(int num, long goldLimit, long goldUpper) {
        Map<Long, Player> acquirePlayers = new ConcurrentHashMap<>();
        for (Player player : RobotMgr.getInstance().getAllRobots().values()) {
            if (player.isPlay()) {
                continue;
            }
            player.setPlay(true);
            acquirePlayers.put(player.getId(), player);
            player.setGold(RandomUtil.randomLong(goldLimit, goldUpper + 1));
            this.updatePlayer(player);
            if (acquirePlayers.size() >= num) {
                return acquirePlayers;
            }
        }
        return acquirePlayers;
    }
}