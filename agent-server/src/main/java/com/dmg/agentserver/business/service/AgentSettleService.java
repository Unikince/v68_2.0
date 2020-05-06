package com.dmg.agentserver.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.agentserver.business.dao.AgentSettleDao;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordG30dsrfcRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifaudRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifciRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGysfcRes;

/**
 * 代理结算
 */
@Service
public class AgentSettleService {
    @Autowired
    private AgentSettleDao dao;
    @Autowired
    private AgentRelationService agentRelationService;

    /**
     * 后台玩家详情获取结算信息
     */
    public AgentSettleRecordGifaudRes getInfoForAdminUserDetails(long playerId) {
        return this.dao.getInfoForAdminUserDetails(playerId);
    }

    /**
     * 客户端首页获取结算信息
     */
    public AgentSettleRecordGifciRes getInfoForClientIndex(long playerId) {
        // 获取从下到上排序后的玩家团队
//        List<AgentRelation> teamRelations = this.agentRelationService.getDownToUpRelation(playerId);
        // TODO 客户端首页获取结算信息
        return null;
    }

    /**
     * 客户端获取昨日结算
     */
    public AgentSettleRecordGysfcRes getYestorySettleForClient(long playerId) {
        // TODO 客户端获取昨日结算
        return null;
    }

    /**
     * 客户端获取30天结算纪录
     */
    public AgentSettleRecordG30dsrfcRes get30DaySettleRecordForClient(long playerId) {
        // TODO 客户端获取30天结算纪录
        return null;
    }
}