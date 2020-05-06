package com.dmg.bairenlonghu.process;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.common.result.ResultEnum;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.model.Seat;
import com.dmg.bairenlonghu.service.PushService;
import com.dmg.bairenlonghu.service.cache.PlayerService;
import com.dmg.bairenlonghu.tcp.server.AbstractMessageHandler;
import com.dmg.bairenlonghu.tcp.server.MessageIdConfig;

/**
 * @Description 获取场外玩家列表
 * @Author mice
 * @Date 2019/8/13 14:09
 * @Version V1.0
 **/
@Service
public class GetOutfiledPlayerListProcess implements AbstractMessageHandler {
    @Autowired
    private PlayerService playerCacheService;
    @Autowired
    private PushService pushService;

    @Override
    public String getMessageId() {
        return MessageIdConfig.GET_OUTFILED_PLAYER_LIST;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        BasePlayer basePlayer = this.playerCacheService.getPlayer(userId);
        Room room = RoomManager.intance().getRoom(basePlayer.getRoomId());
        List<BasePlayer> basePlayerList = new LinkedList<>();
        if (room.getSeatMap().size() > 8) {
            synchronized (room) {
                for (Seat seat : room.getSeatMap().values()) {
                    if (!room.getInfieldSeatMap().containsKey(seat.getSeatIndex() + "")) {
                        basePlayerList.add(seat.getPlayer());
                    }
                }
            }
        }
        this.pushService.push(userId, MessageIdConfig.GET_OUTFILED_PLAYER_LIST, ResultEnum.SUCCESS.getCode(), basePlayerList);
    }
}