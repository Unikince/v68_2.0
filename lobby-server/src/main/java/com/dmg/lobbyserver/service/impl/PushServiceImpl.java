package com.dmg.lobbyserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.PushService;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
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

    public void push(Long userId,String m,Integer res,Object msg){
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        //log.info("==>推送消息{}给:{},结果为{},内容为:{}",m,userId,res,JSONObject.toJSONString(msg,WriteNullStringAsEmpty,WriteNullListAsEmpty,DisableCircularReferenceDetect));
        if (myWebSocket != null) {
            synchronized (myWebSocket.getSession()){
                MessageResult messageResult = new MessageResult(res, msg, m);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult,WriteNullStringAsEmpty,WriteNullListAsEmpty,DisableCircularReferenceDetect));
            }
        }
    }

    @Override
    public void push(Long userId, String m, Integer res) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            synchronized (myWebSocket.getSession()) {
                MessageResult messageResult = new MessageResult(res, "", m);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
            }
        }
    }

    @Override
    public void push(Long userId, String m) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            synchronized (myWebSocket.getSession()) {
                MessageResult messageResult = new MessageResult(ResultEnum.SUCCESS.getCode(), "", m);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
            }
        }
    }

    @Override
    public void push(Long userId, MessageResult messageResult) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            synchronized (myWebSocket.getSession()) {
                //log.info("==>推送消息{}给:{},内容为:{}",messageResult.getM(),userId,JSONObject.toJSONString(messageResult,WriteNullStringAsEmpty,WriteNullListAsEmpty,DisableCircularReferenceDetect));
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
            }
        }
    }
}