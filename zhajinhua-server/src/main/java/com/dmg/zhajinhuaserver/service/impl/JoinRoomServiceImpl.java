package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.*;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.JoinRoomService;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.SitDownService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dmg.zhajinhuaserver.config.MessageConfig.JION_ROOM;

/**
 * @Description 加入房间
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
@Slf4j
@Service
public class JoinRoomServiceImpl implements JoinRoomService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PushService pushService;

    @Autowired
    private SitDownService sitDownService;

    @Autowired
    private PlayerService playerCacheService;

    @Override
    public void joinRoom(Player player, int roomId) {
        log.info("玩家：{}，加入房间：{}", player.getRoleId(), roomId);
        if (roomService.checkRejoinRoom(player, roomId)) {
            log.info("玩家：{}，加入房间：{},[断线重连]", player.getRoleId(), roomId);
            roomService.rejionRoom(player);
            return;
        }
        GameRoom room = RoomManager.instance().getRoom(roomId);
        if (room == null) {
            pushService.push(player.getRoleId(), JION_ROOM, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        if (room.getRoomStatus() == 0) {
            room.setRoomStatus(Config.RoomStatus.READY);
        }
        if (room.getSeatMap().values().size() > 0) {
            boolean hasPlayer = false;
            for (Seat value : room.getSeatMap().values()) {
                if (!(value.getPlayer() instanceof Robot)) {
                    hasPlayer = true;
                    break;
                }
            }
            if (room.getWatchList().size() > 0) {
                hasPlayer = true;
            }
            if (!hasPlayer) {
                room.clearAll();
            }
        }
        Map<String, Object> tableInfo = roomService.roomMsg(room, roomService.getSeat(room, player));
        if (room.getWatchList().size() >= 50) {
            pushService.push(player.getRoleId(), JION_ROOM, ResultEnum.ROOM_NO_EXIST.getCode());
            return;
        }
        if (player.getGold() < room.getLowerLimit().doubleValue()) {
            pushService.push(player.getRoleId(), JION_ROOM, ResultEnum.PLAYER_HAS_NO_MONEY.getCode());
            return;
        }
        if (room.getGameRoomTypeId() == D.PRIVATE_FIELD
                && room.getRound() >= 1 && !room.getCustomRule().isCanIn()) {
            pushService.push(player.getRoleId(), JION_ROOM, ResultEnum.ROOM_IS_GAME.getCode());
            return;
        }
        synchronized (room) {
            if (room.getSeatMap().size() >= room.getTotalPlayer()) {
                log.error("房间人数达到上限-room=" + room.toString());
                pushService.push(player.getRoleId(), JION_ROOM, ResultEnum.ROOM_HAS_FULL.getCode());
                return;
            } else {
                room.setDeadTime(0);
                room.getWatchList().add(player);
            }
        }
        if (!(player instanceof Robot)) {
            RoomManager.instance().getPlayerRoomIdMap().put(player.getRoleId(), roomId);
            GameOnlineChangeUtils.incOnlineNum(Constant.GAME_ID, room.getGrade());
        }
        player.setRoomId(roomId);
        playerCacheService.update(player);
        Map<String, Object> actionInfo = new HashMap<>();
        Map<String, Object> map = new ConcurrentHashMap<>();
        Seat actionSeat = room.getSeatMap().get(room.getPlaySeat());
        if (room.getRoomStatus() == Config.RoomStatus.GAME) {
            if (actionSeat == null || room.getRoomStatus() != Config.RoomStatus.GAME) {
                tableInfo.put(D.SEAT_ACTION_INFO, "");
            } else {
                actionInfo.put(D.PLAYER_RID, actionSeat.getPlayer().getRoleId());
                actionInfo.put(D.TABLE_ACTION_SEAT_INDEX, actionSeat.getSeatId());
                actionInfo.put(D.SEAT_ACTION_TO_TIME, actionSeat.getActionEndTime());
                actionInfo.put(D.PLAYER_BASE_ACTION_OPER, actionSeat.getActionOper());
                actionInfo.put(D.SEAT_FOLLOW_CHIPS, room.getLastBetChips());
                actionInfo.put(D.SEAT_FOLLOW_CHIPS, room.getLastBetChips());
                actionInfo.put(ZhaJinHuaD.IS_RUSH, room.isHaveRush());
                actionInfo.put(ZhaJinHuaD.RUSH_ID, room.getBloodId());
                tableInfo.put(D.SEAT_ACTION_INFO, actionInfo);
            }
        }
        map.put(D.TABLE_INFO, tableInfo);
        MessageResult messageResult = new MessageResult(JION_ROOM, map);
        pushService.push(player.getRoleId(), messageResult);
        if (player instanceof Robot) {
            sitDownService.sitDown(player, D.DEFINENUM);
        }
    }
}
