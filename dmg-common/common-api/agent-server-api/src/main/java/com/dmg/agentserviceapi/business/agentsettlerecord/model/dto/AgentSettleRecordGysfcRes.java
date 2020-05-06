package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理结算纪录--客户端获取昨日结算--返回
 */
@Data
public class AgentSettleRecordGysfcRes {
    /** 结算佣金 */
    private BigDecimal myBrokerage;
    /** 昨日业绩 */
    private BigDecimal subTeamPerformance;
    /** 返佣等级 */
    private String levelName;
}
