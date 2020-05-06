package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.common;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理结算_基础_公用_返回
 */
@Data
public class AgentSettleCommonRes {
    /** 自营业绩 */
    private BigDecimal myPerformance;
    /** 直属下级业绩 */
    private BigDecimal subDirectlyPerformance;
    /** 下级团队业绩 */
    private BigDecimal subTeamPerformance;
}
