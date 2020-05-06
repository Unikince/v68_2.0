package com.dmg.agentserviceapi.business.agentrelation.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationBindReq;
import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationCreateReq;
import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationGifaudRes;
import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationGifciRes;
import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationTransferReq;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ResultAgent;

/**
 * 代理关系
 */
@FeignClient(value = "AGENT-SERVER", path = "/agent-api/agentRelation")
public interface AgentRelationFeign {

    /**
     * 创建代理关系对象
     */
    @PostMapping(value = "/create")
    ResultAgent<Void> create(@RequestBody AgentRelationCreateReq req);

    /**
     * 绑定代理关系
     */
    @PostMapping(value = "/bind")
    ResultAgent<Void> bind(@RequestBody AgentRelationBindReq req);

    /**
     * 代理转移
     */
    @PostMapping(value = "/transfer")
    ResultAgent<Void> transfer(@RequestBody AgentRelationTransferReq req);

    /**
     * 后台玩家详情获取代理信息
     */
    @PostMapping(value = "/getInfoForAdminUserDetails")
    ResultAgent<AgentRelationGifaudRes> getInfoForAdminUserDetails(@RequestBody IdReq req);

    /**
     * 客户端首页获取代理信息
     */
    @PostMapping(value = "/getInfoForClientIndex")
    ResultAgent<AgentRelationGifciRes> getInfoForClientIndex(@RequestBody IdReq req);
}
