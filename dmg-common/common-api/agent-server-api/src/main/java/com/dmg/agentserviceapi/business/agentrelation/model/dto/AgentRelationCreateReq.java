package com.dmg.agentserviceapi.business.agentrelation.model.dto;

import lombok.Data;

/**
 * 代理关系--创建代理关系对象--接收
 */
@Data
public class AgentRelationCreateReq {
    /** 玩家id */
    private long playerId;
    /** 昵称 */
    private String nickName;
}
