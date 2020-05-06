package com.dmg.zhajinhuaserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Robot;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.tcp.manager.LocationManager;
import com.dmg.zhajinhuaserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/1 19:37
 * @Version V1.0
 **/
@Service
@Slf4j
public class PushServiceImpl implements PushService {

    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        PushServiceImpl.locationManager = locationManager;
    }

    public void push(Long userId, String m, Integer res, Object msg) {
        MessageResult messageResult = new MessageResult(res, msg, m);
        this.push(userId, messageResult);
    }

    @Override
    public void push(Long userId, String m, Integer res) {
        MessageResult messageResult = new MessageResult(res, "", m);
        this.push(userId, messageResult);
    }

    @Override
    public void push(Long userId, String m) {
        MessageResult messageResult = new MessageResult(ResultEnum.SUCCESS.getCode(), "", m);
        this.push(userId, messageResult);
    }

    @Override
    public void push(Long userId, MessageResult messageResult) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            //log.info("==>推送消息{}给:{},内容为:{}", messageResult.getM(), userId, JSONObject.toJSONString(messageResult));
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
        }
    }

    @Override
    public void broadcast(MessageResult message, GameRoom room) {
        // 给房间内部玩家推送广播消息
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer() != null) {
                this.push(seat.getPlayer().getRoleId(), message);
            }
        }
        // 给围观列表推送广播
        for (Player player : room.getWatchList()) {
            if (player != null) {
                this.push(player.getRoleId(), message);
            }
        }
    }

    @Override
    public void broadcastWithOutPlayer(MessageResult message, Player player, GameRoom room) {

        for (Map.Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
            if (entry.getValue().getActionSate() == Config.SeatState.STATE_RUN_AWAY) {
                continue;
            }
            Player player0 = entry.getValue().getPlayer();
            if (player0 == null || player0.getRoleId() == player.getRoleId()) {
                continue;
            }
            this.push(player0.getRoleId(), message);
        }
        for (Player player0 : room.getWatchList()) {
            if (player0 != null && player0.getRoleId() != player.getRoleId()) {
                this.push(player0.getRoleId(), message);
            }
        }
    }

    @Override
    public void broadcast(MessageResult message) {
        locationManager.userMap.values().forEach(myWebSocket -> {
            if (myWebSocket != null) {
                Boolean isSend = true;
                for (GameRoom gameRoom : RoomManager.instance().getRoomMap().values()) {
                    if (gameRoom.getRoomStatus() != Config.RoomStatus.STOP) {
                        for (Seat seat : gameRoom.getSeatMap().values()) {
                            if (!(seat.getPlayer() instanceof Robot) && myWebSocket.getAppId().equals(String.valueOf(seat.getPlayer().getRoleId()))) {
                                isSend = false;
                                break;
                            }
                        }
                    }
                }
                if (isSend) {
                    myWebSocket.sendMessage(JSONObject.toJSONString(message, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
                }
            }
        });
    }


}