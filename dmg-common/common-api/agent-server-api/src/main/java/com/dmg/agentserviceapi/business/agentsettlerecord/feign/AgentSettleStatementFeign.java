package com.dmg.agentserviceapi.business.agentsettlerecord.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordCollectExtraRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordCollectReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordCollectRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDatailsCollectDayDataReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDatailsCollectDayDataRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDatailsCollectReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDatailsCollectRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDayDataDetailsReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDayDataDetailsRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDayDataReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDayDataRes;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PageAndExtraPackageRes;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;

/**
 * 代理结算-统计
 */
@FeignClient(value = "AGENT-SERVER", path = "/agent-api/agentSettleStatement")
public interface AgentSettleStatementFeign {
    /**
     * 汇总
     */
    @PostMapping("/collect")
    ResultAgent<PageAndExtraPackageRes<List<AgentSettleRecordCollectRes>, AgentSettleRecordCollectExtraRes>> collect(@RequestBody PagePackageReq<AgentSettleRecordCollectReq> req);

    /**
     * 每日数据
     */
    @PostMapping("/dayData")
    ResultAgent<PagePackageRes<List<AgentSettleRecordDayDataRes>>> dayData(@RequestBody PagePackageReq<AgentSettleRecordDayDataReq> req);

    /**
     * 每日数据_游戏详情
     */
    @PostMapping("/dayDataDetails")
    ResultAgent<PagePackageRes<List<AgentSettleRecordDayDataDetailsRes>>> dayDataDetails(@RequestBody PagePackageReq<AgentSettleRecordDayDataDetailsReq> req);

    /**
     * 游戏详情
     */
    @PostMapping("/datailsCollect")
    ResultAgent<PagePackageRes<List<AgentSettleRecordDatailsCollectRes>>> datailsCollect(@RequestBody PagePackageReq<AgentSettleRecordDatailsCollectReq> req);

    /**
     * 游戏详情_每日数据
     */
    @PostMapping("/datailsCollectDayData")
    ResultAgent<PagePackageRes<List<AgentSettleRecordDatailsCollectDayDataRes>>> datailsCollectDayData(@RequestBody PagePackageReq<AgentSettleRecordDatailsCollectDayDataReq> req);
}
