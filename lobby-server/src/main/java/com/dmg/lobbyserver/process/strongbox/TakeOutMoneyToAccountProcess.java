package com.dmg.lobbyserver.process.strongbox;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

import static com.dmg.lobbyserver.config.MessageConfig.TAKE_OUT_MONEY_TO_ACCOUNT;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_INFO_NTC;
import static com.dmg.lobbyserver.result.ResultEnum.*;

/**
 * @Description 从保险箱中提钱到余额
 * @Author mice
 * @Date 2019/6/20 15:55
 * @Version V1.0
 **/
@Service
@Slf4j
public class TakeOutMoneyToAccountProcess implements AbstractMessageHandler {
    @Value("${md5.salt}")
    private String salt;

    @Autowired
    private UserService userService;
    @Autowired
    private RedPointService redPointService;
    @Autowired
    private UserPositionService userPositionService;

    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        TakeOutMoneyToAccountProcess.locationManager = locationManager;
    }

    @Override
    public String getMessageId() {
        return TAKE_OUT_MONEY_TO_ACCOUNT;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Result<Map<String,Object>> mapResult = userPositionService.getPosition(Integer.parseInt(userid));
        Map<String,Object> data = mapResult.getData();
        if ((int)data.get("gameId") > 0 || (int)(data.get("roomId"))>0){
            result.setRes(BUSINESS_EXCEPTION.getCode());
            return;
        }
        TakeOutMoneyToAccountVO vo = params.toJavaObject(TakeOutMoneyToAccountVO.class);
        UserBean userBean = userService.getUserById(vo.getUserId());
        if (vo.getTakeOutNum().compareTo(BigDecimal.ZERO)<0){
            result.setRes(PARAM_ERROR.getCode());
            return;
        }
        log.info("用户保险箱余额:{},取出:{}",userBean.getStrongboxBalance(),vo.getTakeOutNum());
        if (userBean.getStrongboxBalance().subtract(vo.getTakeOutNum()).compareTo(BigDecimal.ZERO)  < 0) {
            result.setRes(STRONG_BOX_INSUFFICIENT.getCode());
            return;
        }
        String password = DigestUtil.md5Hex(vo.getPassword() + salt);
        if (StringUtils.isNotEmpty(userBean.getStrongboxPassword()) && !userBean.getStrongboxPassword().equals(password)) {
            result.setRes(STRONG_BOX_PASSWORD_ERROR.getCode());
            return;
        }
        BigDecimal strongboxBalance = userBean.getStrongboxBalance();
        BigDecimal accountBalance = userBean.getAccountBalance();
        BigDecimal takeOutNum = vo.getTakeOutNum();
        accountBalance = accountBalance.add(takeOutNum);
        strongboxBalance = strongboxBalance.subtract(takeOutNum);
        userBean.setAccountBalance(accountBalance);
        userBean.setStrongboxBalance(strongboxBalance);
        userService.updateUserById(userBean);
        log.info("==> 用户{},从保险箱提出{}到余额,操作完成后>>>当前余额为{},保险箱余额为{}", vo.getUserId(), vo.getTakeOutNum(), userBean.getAccountBalance(), userBean.getStrongboxBalance());
        MyWebSocket myWebSocket = locationManager.getWebSocket(vo.getUserId());
        if (myWebSocket != null) {
            MessageResult messageResult = new MessageResult(1, "", UPDATE_USER_INFO_NTC);
            myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
        }

    }
}