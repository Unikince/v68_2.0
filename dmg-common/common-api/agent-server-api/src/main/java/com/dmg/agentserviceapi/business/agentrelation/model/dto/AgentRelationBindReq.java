package com.dmg.agentserviceapi.business.agentrelation.model.dto;

import lombok.Data;

/**
 * 代理关系--绑定代理--接收
 */
@Data
public class AgentRelationBindReq {
    /** 玩家id */
    private long playerId;
    /** 上级id */
    private long parentId;
}
