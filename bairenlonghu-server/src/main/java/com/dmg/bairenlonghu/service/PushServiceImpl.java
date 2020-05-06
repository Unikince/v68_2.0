package com.dmg.bairenlonghu.service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenlonghu.common.model.BaseRobot;
import com.dmg.bairenlonghu.common.result.MessageResult;
import com.dmg.bairenlonghu.common.result.ResultEnum;
import com.dmg.bairenlonghu.model.Player;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.model.Seat;
import com.dmg.bairenlonghu.tcp.manager.LocationManager;
import com.dmg.bairenlonghu.tcp.server.MyWebSocket;
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

    public void push(int userId, String m, Integer res, Object msg) {
        MessageResult messageResult = new MessageResult(res, msg, m);
        this.push(userId, messageResult);
    }

    @Override
    public void push(int userId, String m, Integer res) {
        MessageResult messageResult = new MessageResult(res, "", m);
        this.push(userId, messageResult);
    }

    @Override
    public void push(int userId, String m) {
        MessageResult messageResult = new MessageResult(ResultEnum.SUCCESS.getCode(), "", m);
        this.push(userId, messageResult);
    }

    @Override
    public void push(int userId, MessageResult messageResult) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            log.info("==>推送消息{}给:{},内容为:{}", messageResult.getM(), userId, JSONObject.toJSONString(messageResult));
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
        }
    }

    @Override
    public void broadcast(MessageResult message, Room room) {
        if (!room.isSystemBanker()) {
            this.push(room.getBanker().getPlayer().getUserId(), message);
        }
        // 给房间内部玩家推送广播消息
        if (room.getSeatMap().values() == null || room.getSeatMap().values().size() <= 0) {
            return;
        }
        room.getSeatMap().values().forEach(seat -> {
            if (seat.getPlayer() != null && !(seat.getPlayer() instanceof BaseRobot) && !seat.isLeave()) {
                this.push(seat.getPlayer().getUserId(), message);
            }
        });
    }

    @Override
    public void broadcastWithOutPlayer(MessageResult message, Player player, Room room) {
        for (Map.Entry<String, Seat> entry : room.getSeatMap().entrySet()) {
        }
    }

}