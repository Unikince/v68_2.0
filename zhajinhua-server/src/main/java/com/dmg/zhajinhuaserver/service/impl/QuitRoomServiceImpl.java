package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.config.D;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.*;
import com.dmg.zhajinhuaserver.model.constants.GameConfig;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.QuitRoomService;
import com.dmg.zhajinhuaserver.service.ReadyService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_LEAVEROOM;
import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_LEAVEROOMNTC;

/**
 * @author mice
 * @description: 退出房间
 * @return
 * @date 2019/7/10
 */
@Service
@Slf4j
public class QuitRoomServiceImpl implements QuitRoomService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PushService pushService;

    @Autowired
    private PlayerService playerCacheService;

    @Override
    public boolean quitRoom(Player player, boolean bool, int leaveReason) {
        if (player.getRoomId() == 0) {
            return false;
        }
        GameRoom room = roomService.getRoom(player.getRoomId());
        if (room == null) {
            pushService.push(player.getRoleId(), PLAYER_LEAVEROOM, ResultEnum.ROOM_HAS_STARTED.getCode());
            return false;
        }
        int seat = roomService.getPlaySeat(room, player);
        if (seat == 0) {
            room.getWatchList().remove(player);
            pushService.push(player.getRoleId(), PLAYER_LEAVEROOM, ResultEnum.PLAYER_HAS_NO_SEAT.getCode());
            if (!(player instanceof Robot)) {
                player.setRoomId(0);
                playerCacheService.update(player);
            }
            return false;
        } else {
            if (player instanceof Robot) {
                player.setPlayRounds(0);
                player.setWinLostGold(new BigDecimal("0"));
            }
            if (bool) {
                synchronized (room) {
                    room.getSeatMap().remove(seat);
                    Map<String, Object> map = new HashMap<>();
                    map.put(D.PLAYER_RID, player.getRoleId());
                    map.put(D.LEAVE_ROOM_MESSAGE_REASON, player.getLeaveReason());
                    MessageResult messageResult = new MessageResult(PLAYER_LEAVEROOMNTC, map);
                    pushService.push(player.getRoleId(), messageResult);
                    if (player instanceof Robot) {
                        player.setRoomId(0);
                    } else {
                        player.setRoomId(0);
                        if (player.getLeaveReason() != Config.LeaveReason.LEAVE_OFFLINE) {
                            GameOnlineChangeUtils.decOnlineNum(Constant.GAME_ID, room.getGrade());
                        }
                    }
                    playerCacheService.update(player);
                    room.getWatchList().remove(player);
                }
            } else {
                Seat playerSeat = room.getSeatMap().get(seat);
                if (playerSeat.getState() == Config.SeatState.STATE_FOLD || ((!room.isReady() || playerSeat.getHand().size() == 0) && !playerSeat.isReady())) {
                    synchronized (room) {
                        room.getSeatMap().remove(seat);
                        Map<String, Object> map = new HashMap<>();
                        map.put(D.PLAYER_RID, player.getRoleId());
                        map.put(D.LEAVE_ROOM_MESSAGE_REASON, leaveReason);

                        MessageResult messageResult = new MessageResult(PLAYER_LEAVEROOMNTC, map);
                        pushService.push(player.getRoleId(), messageResult);
                        room.getWatchList().remove(player);
                        room.setPlayerNumber(room.getPlayerNumber() - 1);
                        if (player instanceof Robot) {
                            player.setRoomId(0);
                        } else {
                            player.setRoomId(0);
                            playerCacheService.update(player);
                            if (player.getLeaveReason() != Config.LeaveReason.LEAVE_OFFLINE) {
                                GameOnlineChangeUtils.decOnlineNum(Constant.GAME_ID, room.getGrade());
                            }
                        }
                        // 通知其他玩家此玩家退出房间
                        pushService.broadcast(messageResult, room);
                        //readyService.checkAllReady(room);
                    }
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("runawayMoney", GameConfig.RUN_AWAY_BET * room.getBaseScore());
                    pushService.push(player.getRoleId(), PLAYER_LEAVEROOM, ResultEnum.ROOM_HAS_STARTED.getCode());
                    return false;
                }
            }
        }
        if (!(player instanceof Robot)) {
            RoomManager.instance().getPlayerRoomIdMap().remove(player.getRoleId());
        }
        return true;
    }
}
