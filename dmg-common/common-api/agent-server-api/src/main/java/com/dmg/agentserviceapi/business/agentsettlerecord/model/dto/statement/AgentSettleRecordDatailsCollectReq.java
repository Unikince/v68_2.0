package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement;

import lombok.Data;

/**
 * 代理结算纪录_游戏详情_请求
 */
@Data
public class AgentSettleRecordDatailsCollectReq {
    /** 玩家id,不能为空 */
    private long playerId;
}