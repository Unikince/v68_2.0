package com.dmg.lobbyserver.process.washthecode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.AccountChangeLogBean;
import com.dmg.lobbyserver.dao.bean.SysVipPrivilegeConfigBean;
import com.dmg.lobbyserver.model.dto.SysVipPrivilegeConfigDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.AccountChangeLogService;
import com.dmg.lobbyserver.service.SysConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dmg.lobbyserver.config.MessageConfig.CODE_WASH_INFO;

/**
 * @Description 洗码比例显示(洗码金额)
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Service
@Slf4j
public class CodeinfoProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return CODE_WASH_INFO;
    }

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private AccountChangeLogService accountChangeLogService;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {

        //查询游戏的洗码比例
        List<SysVipPrivilegeConfigBean> sysVipPrivilegeConfigBeans = sysConfigService.getSysVipPrivilegeConfig();

        Map<Integer, List<Double>> VIPWashCodeRatioMap = sysConfigService.getVIPWashCodeRatioMap();
        //查询用户的各游戏的流水
        List<AccountChangeLogBean> accountChangeLogBeanList = accountChangeLogService.getListByUserId(Long.parseLong(userid));
        List<BigDecimal> Money = new ArrayList<>();
        //接收总洗码金额
        BigDecimal finalMoney = BigDecimal.ZERO;
        SysVipPrivilegeConfigDTO SysVipPrivilegeConfigDTO = new SysVipPrivilegeConfigDTO();
        for (AccountChangeLogBean accountChangeLogBean : accountChangeLogBeanList) {
            for (SysVipPrivilegeConfigBean sysVipPrivilegeConfigBean : sysVipPrivilegeConfigBeans) {
                //游戏相同时
                if (accountChangeLogBean.getType() == sysVipPrivilegeConfigBean.getGameType()) {
                    //洗码金额=各游戏比例*各游戏流水之和
                    Money.add(accountChangeLogBean.getAccount().multiply(BigDecimal.valueOf(sysVipPrivilegeConfigBean.getWashCodeRatio())));
                }
            }
        }
        //之和
        finalMoney = Money.stream().map(x -> x).reduce(BigDecimal.ZERO, BigDecimal::add);
        SysVipPrivilegeConfigDTO.setWashCodeRatioMap(VIPWashCodeRatioMap);
        BigDecimal M1 = finalMoney.divide(BigDecimal.valueOf(100));
        SysVipPrivilegeConfigDTO.setMonney(M1);
        if (SysVipPrivilegeConfigDTO != null) {
            result.setMsg(JSON.toJSON(SysVipPrivilegeConfigDTO));
        } else {
            result.setRes(ResultEnum.SYSTEM_DATA_ISNULL.getCode());
        }
    }
}
