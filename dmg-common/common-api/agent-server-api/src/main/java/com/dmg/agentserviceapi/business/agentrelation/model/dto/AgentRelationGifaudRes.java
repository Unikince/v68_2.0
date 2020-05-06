package com.dmg.agentserviceapi.business.agentrelation.model.dto;

import java.util.List;

import lombok.Data;

/**
 * 代理关系--后台玩家详情获取代理信息--返回
 */
@Data
public class AgentRelationGifaudRes {
    /** 玩家id */
    private long playerId;
    /** 上级玩家id */
    private long parentId;
    /** 直属下级数量 */
    private int subPeopleNum;
    /** 下级团队数量 */
    private int teamPeopleNum;
    /** 直属下级信息列表 */
    private List<AgentRelationGifaudRes> subInfoList;
}
