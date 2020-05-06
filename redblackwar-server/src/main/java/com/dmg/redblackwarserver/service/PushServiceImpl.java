package com.dmg.redblackwarserver.service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.redblackwarserver.common.model.BaseRobot;
import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.common.result.ResultEnum;
import com.dmg.redblackwarserver.model.Player;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.model.Seat;
import com.dmg.redblackwarserver.tcp.manager.LocationManager;
import com.dmg.redblackwarserver.tcp.server.MyWebSocket;
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

    public void push(int userId,String m,Integer res,Object msg){
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        //log.info("==>推送消息{}给:{},结果为{},内容为:{}",m,userId,res,JSONObject.toJSONString(msg));
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(res, msg, m);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult,WriteNullStringAsEmpty,WriteNullListAsEmpty,DisableCircularReferenceDetect));
        }
    }

    @Override
    public void push(int userId, String m, Integer res) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(res, "", m);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult,WriteNullStringAsEmpty,WriteNullListAsEmpty,DisableCircularReferenceDetect));
        }
    }

    @Override
    public void push(int userId, String m) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(ResultEnum.SUCCESS.getCode(), "", m);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult,WriteNullStringAsEmpty,WriteNullListAsEmpty,DisableCircularReferenceDetect));
        }
    }

    @Override
    public void push(int userId, MessageResult messageResult) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult,WriteNullStringAsEmpty,WriteNullListAsEmpty,DisableCircularReferenceDetect));
        }
    }

    @Override
    public void broadcast(MessageResult message, Room room) {
        // 给房间内部玩家推送广播消息
        for (Seat seat : room.getSeatMap().values()) {
            if (seat.getPlayer() != null && !(seat.getPlayer() instanceof BaseRobot)&&!seat.isLeave()) {
                this.push(seat.getPlayer().getUserId(),message);
            }
        }
    }

    @Override
    public void broadcastWithOutPlayer(MessageResult message, Player player,Room room) {

        for (Map.Entry<String, Seat> entry : room.getSeatMap().entrySet()) {

        }
    }

    @Override
    public void broadcast(MessageResult message) {
        locationManager.userMap.values().forEach(myWebSocket -> {
            if (myWebSocket != null) {
                myWebSocket.sendMessage(JSONObject.toJSONString(message, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
            }
        });
    }

}