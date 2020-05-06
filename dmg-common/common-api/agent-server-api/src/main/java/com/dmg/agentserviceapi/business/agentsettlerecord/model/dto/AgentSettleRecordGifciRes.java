package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理结算纪录--客户端首页获取结算信息--返回
 */
@Data
public class AgentSettleRecordGifciRes {
    /** 今日自营业绩 */
    private BigDecimal myPerformance;
    /** 今日直属下级业绩 */
    private BigDecimal subDirectlyPerformance;
    /** 今日下级团队业绩 */
    private BigDecimal subTeamPerformance;
    /** 昨日返佣 */
    private BigDecimal myBrokerage;
    /** 历史返佣 */
    private BigDecimal historyBrokerage;
}
