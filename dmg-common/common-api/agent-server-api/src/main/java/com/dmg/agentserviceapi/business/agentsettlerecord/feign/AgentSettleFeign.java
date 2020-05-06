package com.dmg.agentserviceapi.business.agentsettlerecord.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordG30dsrfcRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifaudRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifciRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGysfcRes;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ResultAgent;

/**
 * 代理结算
 */
@FeignClient(value = "AGENT-SERVER", path = "/agent-api/agentSettle")
public interface AgentSettleFeign {
    /**
     * 后台玩家详情获取结算信息
     */
    @PostMapping(value = "/getInfoForAdminUserDetails")
    ResultAgent<AgentSettleRecordGifaudRes> getInfoForAdminUserDetails(@RequestBody IdReq req);

    /**
     * 客户端首页获取结算信息
     */
    @PostMapping(value = "/getInfoForClientIndex")
    ResultAgent<AgentSettleRecordGifciRes> getInfoForClientIndex(@RequestBody IdReq req);

    /**
     * 客户端获取昨日结算
     */
    @PostMapping(value = "/getYestorySettleForClient")
    ResultAgent<AgentSettleRecordGysfcRes> getYestorySettleForClient(@RequestBody IdReq req);

    /**
     * 客户端获取30天结算纪录
     */
    @PostMapping(value = "/get30DaySettleRecordForClient")
    ResultAgent<AgentSettleRecordG30dsrfcRes> get30DaySettleRecordForClient(@RequestBody IdReq req);
}
