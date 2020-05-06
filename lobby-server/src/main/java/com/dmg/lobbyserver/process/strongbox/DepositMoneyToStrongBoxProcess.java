package com.dmg.lobbyserver.process.strongbox;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.DepositMoneyToStrongBoxVO;
import com.dmg.lobbyserver.model.vo.TakeOutMoneyToAccountVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.Result;
import com.dmg.lobbyserver.service.RedPointService;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.feign.UserPositionService;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static com.dmg.lobbyserver.config.MessageConfig.DEPOSIT_MONEY_TO_STRONG_BOX;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_INFO_NTC;
import static com.dmg.lobbyserver.result.ResultEnum.*;

/**
 * @Description 从余额存钱到保险箱
 * @Author mice
 * @Date 2019/6/20 15:55
 * @Version V1.0
 **/
@Service
@Slf4j
public class DepositMoneyToStrongBoxProcess implements AbstractMessageHandler {
    @Autowired
    private UserService userService;
    @Autowired
    private UserPositionService userPositionService;

    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        DepositMoneyToStrongBoxProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return DEPOSIT_MONEY_TO_STRONG_BOX;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Result<Map<String,Object>> mapResult = userPositionService.getPosition(Integer.parseInt(userid));
        Map<String,Object> data = mapResult.getData();
        if ((int)data.get("gameId") > 0 || (int)(data.get("roomId"))>0){
            result.setRes(BUSINESS_EXCEPTION.getCode());
            return;
        }
        DepositMoneyToStrongBoxVO vo = params.toJavaObject(DepositMoneyToStrongBoxVO.class);
        if (vo.getDepositNum().compareTo(BigDecimal.ZERO)<0){
            result.setRes(PARAM_ERROR.getCode());
            return;
        }
        UserBean userBean = userService.getUserById(vo.getUserId());
        if (userBean.getAccountBalance().subtract(vo.getDepositNum()).compareTo(BigDecimal.ZERO) < 0) {
            result.setRes(ACCOUNT_INSUFFICIENT.getCode());
            return;
        }
        BigDecimal strongboxBalance = userBean.getStrongboxBalance();
        BigDecimal accountBalance = userBean.getAccountBalance();
        BigDecimal depositNum = vo.getDepositNum();
        accountBalance = accountBalance.subtract(depositNum);
        strongboxBalance = strongboxBalance.add(depositNum);
        userBean.setAccountBalance(accountBalance);
        userBean.setStrongboxBalance(strongboxBalance);
        userService.updateUserById(userBean);
        log.info("==> 用户{},从余额存{}到保险箱,操作完成后>>>当前余额为{},保险箱余额为{}", vo.getUserId(), vo.getDepositNum(), userBean.getAccountBalance(), userBean.getStrongboxBalance());
        MyWebSocket myWebSocket = locationManager.getWebSocket(vo.getUserId());
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(1, "", UPDATE_USER_INFO_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }

    }
}