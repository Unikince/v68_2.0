package com.dmg.gameconfigserver.controller.statement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserviceapi.business.agentsettlerecord.feign.AgentSettleStatementFeign;
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
import com.dmg.agentserviceapi.core.error.ErrorResult;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PageAndExtraPackageRes;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;

/**
 * 返佣报表
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "statement/agentSettle")
public class AgentSettleStatementController extends BaseController {
    @Autowired
    private AgentSettleStatementFeign feign;

    /**
     * 汇总
     */
    @PostMapping(value = "/collect")
    @SuppressWarnings("rawtypes")
    public Result collect(@RequestBody PagePackageReq<AgentSettleRecordCollectReq> req) {
        ResultAgent<PageAndExtraPackageRes<List<AgentSettleRecordCollectRes>, AgentSettleRecordCollectExtraRes>> result = this.feign.collect(req);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

    /**
     * 每日数据
     */
    @PostMapping(value = "/dayData")
    @SuppressWarnings("rawtypes")
    public Result dayData(@RequestBody PagePackageReq<AgentSettleRecordDayDataReq> req) {
        ResultAgent<PagePackageRes<List<AgentSettleRecordDayDataRes>>> result = this.feign.dayData(req);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

    /**
     * 每日数据_游戏详情
     */
    @PostMapping(value = "/dayDataDetails")
    @SuppressWarnings("rawtypes")
    public Result dayDataDetails(@RequestBody PagePackageReq<AgentSettleRecordDayDataDetailsReq> req) {
        ResultAgent<PagePackageRes<List<AgentSettleRecordDayDataDetailsRes>>> result = this.feign.dayDataDetails(req);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

    /**
     * 游戏详情
     */
    @PostMapping(value = "/datailsCollect")
    @SuppressWarnings("rawtypes")
    public Result datailsCollect(@RequestBody PagePackageReq<AgentSettleRecordDatailsCollectReq> req) {
        ResultAgent<PagePackageRes<List<AgentSettleRecordDatailsCollectRes>>> result = this.feign.datailsCollect(req);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

    /**
     * 游戏详情_每日数据
     */
    @PostMapping(value = "/datailsCollectDayData")
    @SuppressWarnings("rawtypes")
    public Result datailsCollectDayData(@RequestBody PagePackageReq<AgentSettleRecordDatailsCollectDayDataReq> req) {
        ResultAgent<PagePackageRes<List<AgentSettleRecordDatailsCollectDayDataRes>>> result = this.feign.datailsCollectDayData(req);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

}