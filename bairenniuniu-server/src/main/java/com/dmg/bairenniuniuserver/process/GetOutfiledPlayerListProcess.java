package com.dmg.bairenniuniuserver.process;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.bairenniuniuserver.common.result.ResultEnum;
import com.dmg.bairenniuniuserver.manager.RoomManager;
import com.dmg.bairenniuniuserver.model.Room;
import com.dmg.bairenniuniuserver.model.Seat;
import com.dmg.bairenniuniuserver.service.PushService;
import com.dmg.bairenniuniuserver.service.cache.PlayerService;
import com.dmg.bairenniuniuserver.tcp.server.AbstractMessageHandler;
import com.dmg.bairenniuniuserver.tcp.server.MessageIdConfig;

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