package com.dmg.agentserver.business.controller;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserver.business.service.AgentConfigService;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
import com.dmg.agentserviceapi.business.agentconfig.model.pojo.AgentConfig;
import com.dmg.agentserviceapi.core.error.ResultAgent;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

/**
 * 代理配置
 */
@RestController
@RequestMapping("/agent-api/agentConfig")
public class AgentConfigController {
    @Autowired
    private AgentConfigService service;

    /**
     * 修改
     */
    @PostMapping(value = "/update")
    public ResultAgent<Void> update(@RequestBody AgentConfig obj) {
        {// begin:佣金结算时间
            String brokerageSettleTime = obj.getBrokerageSettleTime();
            if (StringUtils.isBlank(brokerageSettleTime)) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "brokerageSettleTime");
            }
            try {
                DateTime d = DateUtil.parse(brokerageSettleTime, DatePattern.NORM_TIME_PATTERN);
                brokerageSettleTime = DateUtil.format(d, DatePattern.NORM_TIME_PATTERN);
            } catch (Exception e) {
                throw BusinessException.create(CommErrorEnum.PARAM_ERROR, "brokerageSettleTime=" + brokerageSettleTime);
            }
            obj.setBrokerageSettleTime(brokerageSettleTime);
        } // end:佣金结算时间

        {// begin:转账开启状态
        } // end:转账开启状态

        {// begin:转账充值下限
            BigDecimal minRecharge = obj.getMinRecharge();
            if (minRecharge == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "minRecharge");
            }
            if (minRecharge.compareTo(BigDecimal.ZERO) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_ZERO, "minRecharge=" + minRecharge);
            }
            String minRechargeStr = minRecharge.toString();
            if (minRechargeStr.length() > 20) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "minRecharge:" + minRechargeStr.length());
            }
            obj.setMinRecharge(minRecharge.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:转账充值下限

        {// begin:转账流水下限
            BigDecimal minWater = obj.getMinWater();
            if (minWater == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "minWater");
            }
            if (minWater.compareTo(BigDecimal.ZERO) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_ZERO, "minWater=" + minWater);
            }
            String minWaterStr = minWater.toString();
            if (minWaterStr.length() > 20) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "minWater:" + minWaterStr.length());
            }
            obj.setMinWater(minWater.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:转账流水下限

        {// begin:转账手续费
            BigDecimal pumpRatio = obj.getPumpRatio();
            if (pumpRatio == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "pumpRatio");
            }
            if (pumpRatio.compareTo(BigDecimal.ZERO) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_ZERO, "pumpRatio=" + pumpRatio);
            }
            String pumpRatioStr = pumpRatio.toString();
            if (pumpRatioStr.length() > 6) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "pumpRatio:" + pumpRatioStr.length());
            }
            obj.setPumpRatio(pumpRatio.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:转账手续费

        {// begin:转账单笔金额下限
            BigDecimal minOncePay = obj.getMinOncePay();
            if (minOncePay == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "minOncePay");
            }
            if (minOncePay.compareTo(BigDecimal.ZERO) <= 0) {
                throw BusinessException.create(CommErrorEnum.LE_ZERO, "minOncePay=" + minOncePay);
            }
            String minOncePayStr = minOncePay.toString();
            if (minOncePayStr.length() > 20) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "Pay:" + minOncePayStr.length());
            }
            obj.setMinOncePay(minOncePay.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:转账单笔金额下限

        {// begin:转账单笔金额上限
            BigDecimal maxOncePay = obj.getMaxOncePay();
            if (maxOncePay == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "maxOncePay");
            }
            if (maxOncePay.compareTo(BigDecimal.ZERO) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_ZERO, "maxOncePay=" + maxOncePay);
            }
            if (maxOncePay.compareTo(obj.getMinOncePay()) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_MIN, "maxOncePay<" + obj.getMinOncePay());
            }
            String maxOncePayStr = maxOncePay.toString();
            if (maxOncePayStr.length() > 20) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "maxOncePay:" + maxOncePayStr.length());
            }
            obj.setMaxOncePay(maxOncePay.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:转账单笔金额上限

        {// begin:日转账次数上限
            int payTimesOfDay = obj.getPayTimesOfDay();
            String payTimesOfDayStr = "" + payTimesOfDay;
            if (payTimesOfDayStr.length() > 4) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "payTimesOfDay:" + payTimesOfDayStr.length());
            }
        } // end:日转账次数上限

        { // begin:日转账金额上限
            BigDecimal payMaxOfDay = obj.getPayMaxOfDay();
            if (payMaxOfDay == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "payMaxOfDay");
            }
            if (payMaxOfDay.compareTo(BigDecimal.ZERO) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_ZERO, "payMaxOfDay=" + payMaxOfDay);
            }
            String payMaxOfDayStr = payMaxOfDay.toString();
            if (payMaxOfDayStr.length() > 20) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "payMaxOfDay:" + payMaxOfDayStr.length());
            }
            obj.setPayMaxOfDay(payMaxOfDay.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:日转账金额上限

        {// begin:转账日起始时间
            String payBeginTime = obj.getPayBeginTime();
            if (StringUtils.isBlank(payBeginTime)) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "payBeginTime");
            }
            try {
                DateTime d = DateUtil.parse(payBeginTime, DatePattern.NORM_TIME_PATTERN);
                payBeginTime = DateUtil.format(d, DatePattern.NORM_TIME_PATTERN);
            } catch (Exception e) {
                throw BusinessException.create(CommErrorEnum.PARAM_ERROR, "payBeginTime=" + payBeginTime);
            }
            obj.setPayBeginTime(payBeginTime);
        } // end:转账日起始时间

        {// begin:转账日起始时间
            String payEndTime = obj.getPayEndTime();
            if (StringUtils.isBlank(payEndTime)) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "payEndTime");
            }
            try {
                DateTime d = DateUtil.parse(payEndTime, DatePattern.NORM_TIME_PATTERN);
                payEndTime = DateUtil.format(d, DatePattern.NORM_TIME_PATTERN);
            } catch (Exception e) {
                throw BusinessException.create(CommErrorEnum.PARAM_ERROR, "payEndTime=" + payEndTime);
            }
            obj.setPayEndTime(payEndTime);
        } // end:转账日起始时间

        {// begin:禁止转账备注
            String forbidRemake = obj.getForbidRemake();
            if (StringUtils.isBlank(forbidRemake)) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "forbidRemake");
            }
            if (forbidRemake.length() > 50) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "forbidRemake:" + forbidRemake.length());
            }
        } // end:禁止转账备注

        this.service.update(obj);
        return ResultAgent.success();
    }

    /**
     * 获取
     */
    @PostMapping(value = "/get")
    public ResultAgent<AgentConfig> get() {
        return ResultAgent.success(this.service.get());
    }

}