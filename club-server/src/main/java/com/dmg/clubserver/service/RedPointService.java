package com.dmg.clubserver.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.process.clubinvitation.ReviewInvitationProcess;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.clubserver.config.MessageConfig.READ_POINT_NTC;
import static com.dmg.clubserver.config.MessageConfig.UPDATE_CLUB_ROOM_CARD_NTC;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/3 15:08
 * @Version V1.0
 **/
@Service
@Slf4j
public class RedPointService {
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        RedPointService.locationManager = locationManager;
    }

    /**
     * @description: 红点推送
     * @param roleId
     * @param redPointType
     * @return int
     * @author mice
     * @date 2019/6/5
    */
    public void push(Integer roleId,Integer redPointType){
        MyWebSocket myWebSocket = locationManager.getWebSocket(roleId);
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(1, redPointType, READ_POINT_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }

    }

}