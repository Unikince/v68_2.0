package com.dmg.niuniuserver.service.action.impl;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.model.constants.RoomStatus;
import com.dmg.niuniuserver.model.constants.SeatState;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.result.ResultEnum;
import com.dmg.niuniuserver.service.PushService;
import com.dmg.niuniuserver.service.RoomService;
import com.dmg.niuniuserver.service.action.ReadyService;

/**
 * @Description 准备
 * @Author mice
 * @Date 2019/7/4 14:17
 * @Version V1.0
 **/
@Service
@Slf4j
public class ReadyServiceImpl implements ReadyService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private PushService pushService;

    @Override
    public void ready(Player player, boolean ready, boolean automaticReady) {
        GameRoom room = this.roomService.getRoom(player.getRoomId());
        if (room == null) {
            this.pushService.push(player.getUserId(), MessageConfig.READY, ResultEnum.ROOM_HAS_NO_EXIST);
            return;
        }
        if (room.getRoomStatus() > RoomStatus.STATE_WAIT_ALL_READY) {
            this.pushService.push(player.getUserId(), MessageConfig.READY, ResultEnum.ROOM_HAS_STARTED);
            return;
        }
        int seatId = this.roomService.getPlayerSeatId(room, player);
        //TODO 检查房间为空 人却在里面
        if (room.getSeatMap().size()==0 ||seatId==0){
            return;
        }
        if (ready) {
            log.warn("房间人数:{},seatId:{}",room.getSeatMap().size(),seatId);
            room.getSeatMap().get(seatId).setStatus(SeatState.STATE_WAIT_START);
        }

        room.getSeatMap().get(seatId).setReady(ready);
        // 如果是自动准备,则不推送这条消息
        if (!automaticReady) {
            this.pushService.push(player.getUserId(), MessageConfig.READY, ResultEnum.SUCCESS);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", player.getUserId());
        map.put("seatId", seatId);
        map.put("tableStatus", room.getRoomStatus());

        MessageResult messageResult = new MessageResult(ResultEnum.SUCCESS.getCode(), map, MessageConfig.PLAYERREADYNTC);
        this.pushService.broadcast(messageResult, room);
        // 检查玩家准备状态, 然后判断是否进入下一步操作
        this.roomService.canEnterDeal(room);
    }
}