package com.dmg.lobbyserver.process.game;

import static com.dmg.lobbyserver.config.MessageConfig.SYNC_USER_GOLD;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_INFO_NTC;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.dao.bean.AccountChangeLogBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.SyncUserGoldVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.AccountChangeLogService;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 同步游戏服务金币
 * @Author mice
 * @Date 2019/7/4 16:35
 * @Version V1.0
 **/
@Service
@Slf4j
public class SyncUserGoldProcess implements AbstractMessageHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountChangeLogService accountChangeLogService;
    
    @Autowired
    private RedisUtil redisUtil;

    private static final int count = 5000;

    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        SyncUserGoldProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return SYNC_USER_GOLD;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        SyncUserGoldVO vo = params.toJavaObject(SyncUserGoldVO.class);
        log.info("玩家{},在{}中变更金额:{}", vo.getUserId(), vo.getType(), vo.getGold());
        UserBean userBean = userService.getUserById(vo.getUserId());
        if (userService.changeAccountBalance(vo.getUserId(), vo.getGold()) != null) {
            AccountChangeLogBean accountChangeLogBean = AccountChangeLogBean.builder()
                    .account(vo.getGold())
                    .createDate(new Date())
                    .modifyDate(new Date())
                    .type(vo.getType())
                    .userId(vo.getUserId())
                    .beforeAccount(userBean.getAccountBalance())
                    .accountNo(System.currentTimeMillis())
                    .afterAccount(userBean.getAccountBalance().add(vo.getGold())).build();
            this.redisUtil.lSet(RedisKey.ACCOUNT_CHANGE_LOG, accountChangeLogBean);
            if (this.redisUtil.lGetListSize(RedisKey.ACCOUNT_CHANGE_LOG) >= count) {
                List<AccountChangeLogBean> accountChangeLogBeanList = new ArrayList<>();
                List<Object> data = redisUtil.lGetAndTrim(RedisKey.ACCOUNT_CHANGE_LOG, 0, -1);
                data.forEach(object -> accountChangeLogBeanList.add((AccountChangeLogBean) object));
                accountChangeLogService.insertList(accountChangeLogBeanList);
            }
        }
        MyWebSocket myWebSocket = locationManager.getWebSocket(vo.getUserId());
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(1, "", UPDATE_USER_INFO_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }
    }
}