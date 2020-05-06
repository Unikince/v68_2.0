package com.dmg.lobbyserver.process.activity;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.SyncUserGoldVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * 用户领取物品
 * Author:刘将军
 * Time:2019/6/20 14:22
 * Created by IntelliJ IDEA Community
 */

@Service
@SuppressWarnings("all")
public class UserProessService {
    @Autowired
    private UserService userService;

    @Autowired
    private AbstractMessageHandler syncUserGoldProcess;

    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        UserProessService.locationManager = locationManager;
    }

    public void getItems(Integer type, Integer itemNumber, String userId) {
        UserBean userBean = userService.getUserById(Long.valueOf(userId));
        switch (type) {
            case 1:
                //金币
                SyncUserGoldVO syncUserGoldVO = new SyncUserGoldVO();
                syncUserGoldVO.setGold(BigDecimal.valueOf(itemNumber));
                syncUserGoldVO.setUserId(Long.parseLong(userId));
                syncUserGoldVO.setType(AccountChangeTypeEnum.CODE_TASK.getCode());
                syncUserGoldProcess.messageHandler(String.valueOf(userId), (JSONObject) JSONObject.toJSON(syncUserGoldVO), null);
                break;
            case 2:
                //积分
                userBean.setIntegral(userBean.getIntegral() + itemNumber);
                userService.updateUserById(userBean);
                break;
            case 3:
                //再接再厉
                break;
            default:
                throw new RuntimeException("物品类型错误！没有相应逻辑进行处理");
        }
    }
}
