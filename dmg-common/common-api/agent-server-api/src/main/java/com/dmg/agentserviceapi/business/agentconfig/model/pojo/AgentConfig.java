package com.dmg.agentserviceapi.business.agentconfig.model.pojo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理配置
 */
@Data
public class AgentConfig {
    /** 佣金结算时间 */
    private String brokerageSettleTime;
    /** 转账开启状态 */
    private boolean transferAccountsStautus;
    /** 转账充值下限 */
    private BigDecimal minRecharge;
    /** 转账流水下限 */
    private BigDecimal minWater;
    /** 转账手续费(百分比) */
    private BigDecimal pumpRatio;
    /** 转账单笔金额下限 */
    private BigDecimal minOncePay;
    /** 转账单笔金额上限 */
    private BigDecimal maxOncePay;
    /** 日转账次数上限 */
    private int payTimesOfDay;
    /** 日转账金额上限 */
    private BigDecimal payMaxOfDay;
    /** 转账日起始时间 */
    private String payBeginTime;
    /** 转账日截止时间 */
    private String payEndTime;
    /** 禁止转账备注 */
    private String forbidRemake;
}
