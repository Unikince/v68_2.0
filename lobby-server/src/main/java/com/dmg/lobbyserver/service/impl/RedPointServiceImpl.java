package com.dmg.lobbyserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.RedPointService;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.READ_POINT_NTC;


/**
 * @Description
 * @Author mice
 * @Date 2019/6/3 15:08
 * @Version V1.0
 **/
@Service
@Slf4j
public class RedPointServiceImpl implements RedPointService {
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        RedPointServiceImpl.locationManager = locationManager;
    }

    public void push(Long userId,String redPointType,boolean show){
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            JSONObject redPointInfo = new JSONObject();
            redPointInfo.put("redPointType",redPointType);
            redPointInfo.put("show",show);
            MessageResult messageResult = new MessageResult(1, redPointInfo, READ_POINT_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }
    }
}