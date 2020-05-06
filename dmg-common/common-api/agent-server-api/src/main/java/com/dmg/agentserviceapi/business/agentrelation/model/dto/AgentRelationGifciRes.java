package com.dmg.agentserviceapi.business.agentrelation.model.dto;

import lombok.Data;

/**
 * 代理关系--客户端首页获取代理信息--返回
 */
@Data
public class AgentRelationGifciRes {
    /** 玩家id */
    private long playerId;
    /** 上级玩家id */
    private long parentId;
    /** 直属下级数量 */
    private int subPeopleNum;
    /** 下级团队数量 */
    private int teamPeopleNum;
    /** 今日新增用户 */
    private int newPeopleNum;
}
