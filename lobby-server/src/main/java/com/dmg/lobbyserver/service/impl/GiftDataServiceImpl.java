package com.dmg.lobbyserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.GIFT_DATA;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_INFO_NTC;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/25 17:52
 * @Version V1.0
 **/
@Slf4j
@Service
public class GiftDataServiceImpl implements GiftDataService {
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        GiftDataServiceImpl.locationManager = locationManager;
    }

    @Override
    public void sendGiftData(Long userId, List<GiftDataDTO> giftDataDTOList) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(userId);
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(1, giftDataDTOList, GIFT_DATA);
            log.info("==>推送消息{}给:{},内容为:{}", messageResult.getM(), userId, JSONObject.toJSONString(messageResult));
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            messageResult = new MessageResult(1, "", UPDATE_USER_INFO_NTC);
            log.info("==>推送消息{}给:{},内容为:{}", messageResult.getM(), userId, JSONObject.toJSONString(messageResult));
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        } else {
            log.error("连接不存在:{}", userId);
        }
    }
}