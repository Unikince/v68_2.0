package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理结算纪录_汇总_额外返回
 */
@Data
public class AgentSettleRecordCollectExtraRes {
    /** 总共返佣 */
    private BigDecimal allBrokerage;
}