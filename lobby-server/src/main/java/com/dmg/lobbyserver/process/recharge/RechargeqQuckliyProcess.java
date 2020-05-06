package com.dmg.lobbyserver.process.recharge;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.*;
import com.dmg.lobbyserver.dao.bean.*;
import com.dmg.lobbyserver.model.dto.RechargeDTO;
import com.dmg.lobbyserver.model.vo.RechargeVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.dmg.lobbyserver.config.MessageConfig.PROMOTION_QUCKLIY;

/**
 * @Description 立刻充值
 * @Author jock
 * @Date 2019/7/3 0003
 * @Version V1.0
 **/
@Slf4j
@Service
public class RechargeqQuckliyProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return PROMOTION_QUCKLIY;
    }

    @Autowired
    SysPromotionCodeDao sysPromotionCodeDao;
    @Autowired
    UserService userService;
    @Autowired
    TaskConfigDao taskConfigDao;
    @Autowired
    SysRewardConfigDao sysRewardConfigDao;
    @Autowired
    SysRewardConfigDetailDao sysRewardConfigDetailDao;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        RechargeVO rechargeVO = params.toJavaObject(RechargeVO.class);
        RechargeDTO rechargeDTO = new RechargeDTO();
        if (rechargeVO.getMoney() == null) {
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
//        //判断输入金额是否超出范围
//            Optional<SysRechargeLogBean >  max= moneyList.stream().max(Comparator.comparingInt(SysRechargeLogBean ::getRechargeNumber));
//            SysRechargeLogBean sysRechargeConfigBean1 = max.get();
//            if (rechargeVO.getMoney() >sysRechargeConfigBean1.getRechargeNumber() ) {
//                result.setRes(ResultEnum.RECHARGE_NUMBER_PULL.getCode());
//                return;
//            }
        if (rechargeVO.getMoney() < 20 || rechargeVO.getMoney() > 20000) {
            result.setRes(ResultEnum.RECHARGE_NUMBER_PULL.getCode());
            return;
        }
        //生成订单号
        SimpleDateFormat sn = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sn.format(new Date());
        String res = "";
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            res += random.nextInt(10);
        }
        if (rechargeVO.getTaskId() == null && rechargeVO.getPromotionCodeId() == null) {
            rechargeDTO.setMoney(rechargeVO.getMoney());
            rechargeDTO.setOrderNo(newDate + res);
            result.setMsg(rechargeDTO);
            return;
        }
        if (!StringUtils.isEmpty(rechargeVO.getPromotionCodeId() + "")) {
            SysPromotionCodeBean sysPromotionCodeBean = sysPromotionCodeDao.selectOne(new LambdaQueryWrapper<SysPromotionCodeBean>().
                    eq(SysPromotionCodeBean::getId, rechargeVO.getPromotionCodeId()).
                    eq(SysPromotionCodeBean::getHasReceive, 1));
            UserBean userBean = userService.getUserById(Long.parseLong(userid));
            userBean.setAccountBalance(userBean.getAccountBalance().add(BigDecimal.valueOf(sysPromotionCodeBean.getDepositRebate())));
            userService.updateUserById(userBean);
            rechargeDTO.setMoney(rechargeVO.getMoney());
            rechargeDTO.setOrderNo(newDate + res);
            result.setMsg(rechargeDTO);
            return;
        }
        if (!StringUtils.isEmpty(rechargeVO.getTaskId() + "")) {
            TaskConfigBean taskConfigBean = taskConfigDao.getBean(rechargeVO.getTaskId(), new Date());
            SysRewardConfigBean sysRewardConfigBean = sysRewardConfigDao.selectOne(new LambdaQueryWrapper<SysRewardConfigBean>().
                    eq(SysRewardConfigBean::getId, taskConfigBean.getTaskRewardIds()));
            SysRewardConfigDetailBean sysRewardConfigDetailBean = sysRewardConfigDetailDao.selectOne(new LambdaQueryWrapper<SysRewardConfigDetailBean>().
                    eq(SysRewardConfigDetailBean::getId, sysRewardConfigBean.getRewardDetailId()));
            rechargeDTO.setMoney(rechargeVO.getMoney());
            rechargeDTO.setOrderNo(newDate + res);
            return;
        }
    }
}
