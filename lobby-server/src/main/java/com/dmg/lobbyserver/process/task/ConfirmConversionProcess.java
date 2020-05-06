package com.dmg.lobbyserver.process.task;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.common.enums.ReceiveAwardStatusEnum;
import com.dmg.lobbyserver.dao.IntegralExchangeLogDao;
import com.dmg.lobbyserver.dao.SysConvertibleConfigDao;
import com.dmg.lobbyserver.dao.TaskConfigDao;
import com.dmg.lobbyserver.dao.UserTaskProgressDao;
import com.dmg.lobbyserver.dao.bean.*;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

import static com.dmg.lobbyserver.config.MessageConfig.CONFIRM_CONVERSION_TASK;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_INFO_NTC;

/**
 * @Description 确认兑换
 * @Author jock
 * @Date 2019/6/25 0025
 * @Version V1.0
 **/
@Service
@Slf4j
public class ConfirmConversionProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return CONFIRM_CONVERSION_TASK;
    }

    @Autowired
    UserTaskProgressDao userTaskProgressDao;
    @Autowired
    UserService userService;
    @Autowired
    SysConvertibleConfigDao sysConvertibleConfigDao;
    @Autowired
    TaskConfigDao taskConfigDao;
    @Autowired
    IntegralExchangeLogDao integralExchangeLogDao;
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        ConfirmConversionProcess.locationManager = locationManager;
    }

    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        //当前兑换的积分
        int anInt = params.getIntValue("num");
        if (StringUtils.isEmpty(anInt + "")) {
            result.setRes(ResultEnum.JIFEN_ISNULL.getCode());
            return;
        }
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        if (userBean == null) {
            result.setRes(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            return;
        }
        //用户积分
        int userIntegral = userBean.getIntegral().intValue();
        //兑换比列
        SysConvertibleConfigBean one = sysConvertibleConfigDao.getOne();
        //包装用户总进度数
        Integer userProgress = 0;
        //包装任务进度
        Integer taskProgress = 0;
        //获取当前用户兑换任务进度
        UserTaskProgressBean userTaskProgressBeans = userTaskProgressDao.selectOne(new LambdaQueryWrapper<UserTaskProgressBean>().
                eq(UserTaskProgressBean::getUserId, userid)
                .eq(UserTaskProgressBean::getReceiveAwardStatus, ReceiveAwardStatusEnum.CODE_HAVE_RECEIVED.getCode())
                .eq(UserTaskProgressBean::getTaskType, 4));
        //任务进度
        TaskConfigBean taskConfigBeans = taskConfigDao.selectOne(new LambdaQueryWrapper<TaskConfigBean>().
                eq(TaskConfigBean::getTaskStatus, 1).eq(TaskConfigBean::getTaskType, 4));
        //判断输入积分是否小于用户积分
        if (anInt > userIntegral) {
            result.setRes(ResultEnum.CONVERTI_EXCEPTION.getCode());
            return;
        }
        //判断用户积分是否为0
        if (userBean.getIntegral() == 0) {
            result.setRes(ResultEnum.USER_JIFEN_ISNULL.getCode());
            return;
        }
        //剩余积分
        int i = userIntegral - anInt;
        long b = (int) i;
        double money = anInt * 1.0 / one.getConvertibleProportion();
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String moneyString = dFormat.format(money);
        money = Double.parseDouble(moneyString);
        moneyString = dFormat.format(userBean.getAccountBalance().add(BigDecimal.valueOf(money)));
        double accountBalance = Double.parseDouble(moneyString);
        userBean.setAccountBalance(BigDecimal.valueOf(accountBalance));
        //插入兑换记录
        IntegralExchangeLogBean integralExchangeLogBean = new IntegralExchangeLogBean();
        integralExchangeLogBean.setUserId(Long.parseLong(userid));
        integralExchangeLogBean.setIntegralNumber((long) anInt);
        integralExchangeLogBean.setMoneyNumber(money);
        integralExchangeLogBean.setExchangeDate(new Date());
        UserTaskProgressBean userTaskProgressBean = null;
        if (userTaskProgressBeans != null) {
            userProgress = userTaskProgressBeans.getTaskProgress();
            //判断进度是否超载
            if (userProgress >= taskConfigBeans.getTaskCondition()) {
                log.debug(" The progress is full,userProgress:{},allProgress:{}", userProgress, taskProgress);
                result.setRes(ResultEnum.TASK_ISPULL.getCode());
                return;
            }
            //判断兑换积分是否小于兑换量
            if (anInt + userProgress > taskConfigBeans.getTaskCondition()) {
                result.setRes(ResultEnum.LIMIT_JIFEN.getCode());
                return;
            }

            log.info("The progress is show -------------userProgress:{},allProgress:{}", userProgress, taskProgress);
            //判断输入积分是否小于用户积分
            //修改用户积分及余额
            userBean.setIntegral(b);
            userService.updateUserById(userBean);
            //修改用户进度
            userTaskProgressBeans.setTaskProgress(userTaskProgressBeans.getTaskProgress() + anInt);
            userTaskProgressDao.updateById(userTaskProgressBeans);
            //插入兑换积分
            integralExchangeLogDao.insert(integralExchangeLogBean);
            result.setMsg("成功兑换" + money + "元,已添加至您的账户,请查收!");
            MyWebSocket myWebSocket = locationManager.getWebSocket(Long.parseLong(userid));
            if (myWebSocket != null) {
                MessageResult messageResult = new MessageResult(1, "", UPDATE_USER_INFO_NTC);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        } else {
            userTaskProgressBean = new UserTaskProgressBean();
            userTaskProgressBean.setUserId(Long.parseLong(userid));
            userTaskProgressBean.setTaskProgress(anInt);
            userTaskProgressBean.setCreateDate(new Date());
            userTaskProgressBean.setTaskId(taskConfigBeans.getId());
            userTaskProgressBean.setTaskType(taskConfigBeans.getTaskClassification());
            userTaskProgressBean.setReceiveAwardStatus(ReceiveAwardStatusEnum.CODE_HAVE_RECEIVED.getCode());
            //修改用户余额
            userBean.setIntegral(b);
            userService.updateUserById(userBean);
            //插入用户进度
            userTaskProgressBean.setTaskProgress(anInt);
            userTaskProgressDao.insert(userTaskProgressBean);
            //插入兑换积分
            integralExchangeLogDao.insert(integralExchangeLogBean);
            result.setMsg("成功兑换" + money + "元,已添加至您的账户,请查收!");
            MyWebSocket myWebSocket = locationManager.getWebSocket(Long.parseLong(userid));
            if (myWebSocket != null) {
                MessageResult messageResult = new MessageResult(1, "", UPDATE_USER_INFO_NTC);
                myWebSocket.sendMessage(JSONObject.toJSONString(messageResult));
            }
        }
    }
}
