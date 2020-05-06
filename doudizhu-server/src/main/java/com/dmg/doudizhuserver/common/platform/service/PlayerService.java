package com.dmg.doudizhuserver.common.platform.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dmg.data.client.NettySend;
import com.dmg.data.common.dto.SettleSendDto;
import com.dmg.data.common.dto.SyncRoomSendDto;
import com.dmg.data.common.dto.UserRecvDto;
import com.dmg.data.common.dto.UserSendDto;
import com.dmg.doudizhuserver.common.platform.model.Player;
import com.dmg.doudizhuserver.common.platform.model.Robot;

import cn.hutool.core.util.RandomUtil;

/**
 * 玩家信息
 */
@Service
public class PlayerService {
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
        UserSendDto sendDto = UserSendDto.builder().userId(playerId).build();
        UserRecvDto user = this.nettySend.getUser(sendDto);
        if (user == null || user.getCode() != 0) {
            throw new RuntimeException("玩家[" + playerId + "]不存在");
        }
        Player player = this.playerCacheService.getPlayer(playerId);
        if (player == null) {
            player = new Player();
            player.setId(playerId);
        }
        player.setNickname(user.getNickname());
        player.setHeadImg(user.getHeadImage());
        player.setSex(user.getSex());
        player.setGold(user.getGold());
        this.updatePlayer(player);
        return player;
    }

    /** 结算 */
    public void settle(SettleSendDto sendDto) {
        sendDto.setGameId(this.gameId);
        this.nettySend.settle(sendDto);
    }

    /**
     * 同步房间
     *
     * @param roomLevel 房间级别
     * @param roomId 房间id
     * @param userId 玩家id
     */
    public void syncRoom(int roomLevel, int roomId, long userId) {
        int gameId = this.gameId;
        SyncRoomSendDto sendDto = SyncRoomSendDto.builder().build();
        sendDto.setGameId(gameId);
        sendDto.setRoomLevel(roomLevel);
        sendDto.setRoomId(roomId);
        sendDto.setUserId(userId);
        this.nettySend.syncRoom(sendDto);
    }

    /**
     * 更新玩家信息
     *
     * @param Player 玩家信息
     * @return 玩家信息
     */
    public Player updatePlayer(Player player) {
        return this.playerCacheService.updatePlayer(player);
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
     * 更新玩家信息
     *
     * @param Robot 玩家信息
     * @return 玩家信息
     */
    public Robot updateRobot(Robot robot) {
        return this.playerCacheService.updateRobot(robot);
    }

    /**
     * 获取机器人信息，生成金币值
     *
     * @param playerId 玩家id
     * @param minGold 最小金币
     * @param maxGold 最大金币
     * @return 机器人信息
     */
    public Robot getRobot(long playerId, BigDecimal minGold, BigDecimal maxGold) {
        Robot obj = this.getRobot(playerId);
        if (obj != null) {
            obj.setGold(RandomUtil.randomBigDecimal(minGold, maxGold));
            this.updateRobot(obj);
        }
        return obj;
    }
}