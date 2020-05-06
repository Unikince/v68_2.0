package com.dmg.bairenlonghu.service.logic.impl;

import java.math.BigDecimal;

import com.dmg.bairenlonghu.common.constant.Constant;
import com.dmg.bairenlonghu.sysconfig.RegionConfig;
import com.dmg.common.core.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.common.model.BaseRobot;
import com.dmg.bairenlonghu.common.result.MessageResult;
import com.dmg.bairenlonghu.common.result.ResultEnum;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.model.RoomStatus;
import com.dmg.bairenlonghu.model.Seat;
import com.dmg.bairenlonghu.model.dto.RoomInfoDTO;
import com.dmg.bairenlonghu.model.dto.SettleResultDTO;
import com.dmg.bairenlonghu.service.PushService;
import com.dmg.bairenlonghu.service.cache.PlayerService;
import com.dmg.bairenlonghu.service.cache.RobotCacheService;
import com.dmg.bairenlonghu.service.logic.JoinRoomService;
import com.dmg.bairenlonghu.service.logic.StartGameService;
import com.dmg.bairenlonghu.tcp.server.MessageIdConfig;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/1 10:41
 * @Version V1.0
 **/
@Service
@Slf4j
public class JoinRoomServiceImpl implements JoinRoomService {

    @Autowired
    private PushService pushService;

    @Autowired
    private PlayerService playerCacheService;

    @Autowired
    private RobotCacheService robotCacheService;

    @Autowired
    private StartGameService startGameService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void playerJoinRoom(int userId, int level) {
        BasePlayer basePlayer = this.playerCacheService.getPlayer(userId);
        Room room;
        Seat selfSeat;
        if (basePlayer.getRoomId() > 0) {
            log.info("玩家:{},断线重连", basePlayer.getNickname());
            room = RoomManager.intance().getRoom(basePlayer.getRoomId());
            if (room == null) {
                room = this.filterRoom(level);
                selfSeat = this.initSelfSeat(room, basePlayer);
            } else {
                selfSeat = RoomManager.intance().getSeat(room, userId);
                if (selfSeat == null) {
                    selfSeat = this.initSelfSeat(room, basePlayer);
                }
            }
        } else {
            int goldLimit = RoomManager.intance().getFileLimit(level);
            if (goldLimit > basePlayer.getGold().intValue()) {
                this.pushService.push(userId, MessageIdConfig.JOIN_ROOM, ResultEnum.PLAYER_HAS_NO_MONEY.getCode());
                return;
            }
            room = this.filterRoom(level);
            selfSeat = this.initSelfSeat(room, basePlayer);
        }
        this.playerCacheService.updatePlayer(basePlayer);
        RoomInfoDTO roomInfoDTO = new RoomInfoDTO();
        BeanUtils.copyProperties(room, roomInfoDTO);
        roomInfoDTO.setSelfSeat(selfSeat);
        roomInfoDTO.setReportFormMap(redisUtil.lGet(RegionConfig.GAME_REPORT_FROM_MAP + ":" + Constant.GAME_ID + "_" + room.getLevel(), 0, -1));
        if (room.getRoomStatus() == RoomStatus.SETTLE) {
            try {
                roomInfoDTO.getSettleResultDTO().setSelfSettleInfo(new SettleResultDTO.SettleInfo());
            } catch (Exception e) {
                log.error("{}", e);
                try {
                    if (room.getRoomStatus() == RoomStatus.SETTLE) {
                        roomInfoDTO.getSettleResultDTO().setSelfSettleInfo(new SettleResultDTO.SettleInfo());
                        log.error("---------------:{}", roomInfoDTO.getSettleResultDTO());
                    }
                } catch (Exception e2) {
                    log.error("{}", e2);
                }
            }
        }
        MessageResult messageResult = new MessageResult(MessageIdConfig.JOIN_ROOM, roomInfoDTO);
        this.pushService.push(userId, messageResult);
    }

    @Override
    public void robotJoinRoom() {
        for (Room room : RoomManager.intance().getRoomMap().values()) {
            // TODO 机器人加入策略
            if (room.getPlayerNumber() < 10 && RandomUtil.randomInt(100) > 50) {
                int userId = RandomUtil.randomInt(300) + 1;
                BaseRobot robot = this.robotCacheService.getRobot(userId + 101000);
                if (robot.getRoomId() > 0) {
                    continue;
                }
                robot.setPlayRoundLimit(RandomUtil.randomInt(3, 6));
                robot.setGold(new BigDecimal(RandomUtil.randomInt(100, 3000)));
                this.initSelfSeat(room, robot);
                this.robotCacheService.update(robot);
            }
        }
    }

    /**
     * @param
     * @return com.dmg.bairenniuniuserver.model.Room
     * @description: 过滤房间
     * @author mice
     * @date 2019/7/31
     */
    private Room filterRoom(int roomLevel) {
        for (Room room : RoomManager.intance().getRoomMap().values()) {
            // TODO 过滤策略
            if (room.getLevel() == roomLevel && room.getPlayerNumber() < room.getTotalPlayer()) {
                return room;
            }
        }
        Room room = RoomManager.intance().createRoom(roomLevel);
        this.startGameService.startGame(room.getRoomId());
        return room;
    }

    /**
     * @param room
     * @param basePlayer
     * @return Seat
     * @description: 初始化座位信息
     * @author mice
     * @date 2019/7/31
     */
    private Seat initSelfSeat(Room room, BasePlayer basePlayer) {
        Seat selfSeat = new Seat();
        int lastSeatIndex = 0;
        synchronized (room) {
            if (!(basePlayer instanceof BaseRobot)) {
                if (basePlayer.getUserId() == room.getBanker().getPlayer().getUserId()) {
                    basePlayer.setRoomId(room.getRoomId());
                    if (!(basePlayer instanceof BaseRobot)) {
                        this.playerCacheService.syncRoom(room.getLevel(), basePlayer.getUserId(), room.getLevel());
                    }
                    basePlayer.setSeatIndex(0);
                    room.getBanker().setLeave(false);
                    return room.getBanker();
                }
                for (Seat seat : room.getSeatMap().values()) {
                    if (seat.getPlayer().getUserId() == basePlayer.getUserId()) {
                        basePlayer.setRoomId(room.getRoomId());
                        if (!(basePlayer instanceof BaseRobot)) {
                            this.playerCacheService.syncRoom(room.getLevel(), basePlayer.getUserId(), room.getLevel());
                        }
                        basePlayer.setSeatIndex(seat.getSeatIndex());
                        seat.setLeave(false);
                        return seat;
                    }
                }
            }
            if (room.getSeatMap().size() > 0) {
                lastSeatIndex = room.getSeatMap().get(room.getSeatMap().lastKey()).getSeatIndex();
            }
            log.info("==>机器人:{},加入房间:{},lastSeatIndex:{}", basePlayer.getNickname(), room.getRoomId(), lastSeatIndex);
            selfSeat.setPlayer(basePlayer);
            selfSeat.setSeatIndex(lastSeatIndex + 1);
            room.getSeatMap().put(selfSeat.getSeatIndex() + "", selfSeat);
            room.setPlayerNumber(room.getPlayerNumber() + 1);
            basePlayer.setRoomId(room.getRoomId());
            if (!(basePlayer instanceof BaseRobot)) {
                this.playerCacheService.syncRoom(room.getLevel(), basePlayer.getUserId(), room.getLevel());
            }
            basePlayer.setSeatIndex(selfSeat.getSeatIndex());
            return selfSeat;
        }
    }
}