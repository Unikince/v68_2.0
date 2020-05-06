package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理结算纪录--后台玩家详情获取结算信息--返回
 */
@Data
public class AgentSettleRecordGifaudRes {
    /** 自得返佣 */
    private BigDecimal myBrokerage;
    /** 团队返佣 */
    private BigDecimal teamBrokerage;
    /** 自营业绩 */
    private BigDecimal myPerformance;
    /** 直属下级业绩 */
    private BigDecimal subDirectlyPerformance;
    /** 下级团队业绩 */
    private BigDecimal subTeamPerformance;
}
