package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.*;
import com.dmg.lobbyserver.dao.bean.*;
import com.dmg.server.common.enums.GameType;
import com.dmg.lobbyserver.model.dto.UserinfoDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.AccountChangeLogService;
import com.dmg.lobbyserver.service.SysConfigService;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.USER_INFO;

/**
 * @Description 个人信息显示
 * @Author jock
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
@Service
@Slf4j
public class UserinfoProcess implements AbstractMessageHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private ReceiveAddressDao receiveAddressDao;

    @Autowired
    private UserRechargeLogDao userRechargeLogDao;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private AccountChangeLogService accountChangeLogService;

    @Override
    public String getMessageId() {
        return USER_INFO;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ReceiveAddressBean receiveAddressBean = receiveAddressDao.selectOne(new LambdaQueryWrapper<ReceiveAddressBean>().eq(ReceiveAddressBean::getUserId, userid));
        UserinfoDTO UserinfoDTO = new UserinfoDTO();
        UserinfoDTO.setReceiveAddressBean(receiveAddressBean);
        List<UserRechargeLogBean> userRechargeLogBeans = userRechargeLogDao.getrecordWeek(Long.parseLong(userid));
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        Integer finalRechage = 0;
        BigDecimal finalConsumes = BigDecimal.ZERO;
        //获取本周的充值金额
        for (UserRechargeLogBean userRechargeLogBean : userRechargeLogBeans) {
            Integer rechargeNumber = userRechargeLogBean.getRechargeNumber();
            finalRechage += rechargeNumber;
        }
        //获取本周流水
        List<AccountChangeLogBean> accountChangeLogBeanList = accountChangeLogService.getWeekChange(Long.parseLong(userid));
        for (AccountChangeLogBean accountChangeLogBean : accountChangeLogBeanList) {
            finalConsumes = finalConsumes.add(accountChangeLogBean.getAccount());
        }
        //升级 保级
        SysVipPrivilegeConfigBean sysVipPrivilegeConfigBean = sysConfigService.getGameVipLeveConfig(userBean.getVipLevel() + 1, GameType.BAIREN_NIUNIU.getKey());
        UserinfoDTO.setUpLevelDepositNum(sysVipPrivilegeConfigBean.getUpLevelDepositNum());
        UserinfoDTO.setUpLevelTurnoverNum(sysVipPrivilegeConfigBean.getUpLevelTurnoverNum());
        UserinfoDTO.setSavingsWeek(finalRechage);
        UserinfoDTO.setConsumesWeek(finalConsumes.intValue());
        UserinfoDTO.setReceiveAddressBean(receiveAddressBean);
        result.setMsg(UserinfoDTO);
    }
}
