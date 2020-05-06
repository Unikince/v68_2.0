package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement;

import lombok.Data;

/**
 * 代理结算纪录_汇总_请求
 */
@Data
public class AgentSettleRecordCollectReq {
    /** 玩家id,可以为空 */
    private long playerId;
}