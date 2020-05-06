package com.dmg.agentserver.business.dao;

import org.apache.ibatis.annotations.Mapper;

import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifaudRes;

/**
 * 代理结算
 */
@Mapper
public interface AgentSettleDao {
    /**
     * 后台玩家详情获取结算信息
     */
    AgentSettleRecordGifaudRes getInfoForAdminUserDetails(long playerId);
}
