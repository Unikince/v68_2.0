package com.dmg.agentserver.business.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserver.business.service.AgentSettleStatementService;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
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
import com.dmg.agentserviceapi.core.page.PageReq;

/**
 * 代理结算-统计
 */
@RestController
@RequestMapping("/agent-api/agentSettleStatement")
public class AgentSettleStatementController {
    @Autowired
    private AgentSettleStatementService service;

    /**
     * 汇总
     */
    @PostMapping("/collect")
    public ResultAgent<PageAndExtraPackageRes<List<AgentSettleRecordCollectRes>, AgentSettleRecordCollectExtraRes>> collect(@RequestBody PagePackageReq<AgentSettleRecordCollectReq> req) {
        if (req == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "req");
        }
        PageReq pageReq = req.getPage();
        if (pageReq == null || PageReq.isError(pageReq)) {
            throw BusinessException.create(CommErrorEnum.PAGE_ERROR);
        }

        AgentSettleRecordCollectReq reqVo = req.getParams();
        if (reqVo == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "params");
        }

        PageAndExtraPackageRes<List<AgentSettleRecordCollectRes>, AgentSettleRecordCollectExtraRes> data = this.service.collect(pageReq, reqVo);
        return ResultAgent.success(data);
    }

    /**
     * 每日数据
     */
    @PostMapping("/dayData")
    public ResultAgent<PagePackageRes<List<AgentSettleRecordDayDataRes>>> dayData(@RequestBody PagePackageReq<AgentSettleRecordDayDataReq> req) {
        if (req == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "req");
        }
        PageReq pageReq = req.getPage();
        if (pageReq == null || PageReq.isError(pageReq)) {
            throw BusinessException.create(CommErrorEnum.PAGE_ERROR);
        }

        AgentSettleRecordDayDataReq reqVo = req.getParams();
        if (reqVo == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "params");
        }

        // begin:玩家id,不能为空
        if (reqVo.getPlayerId() <= 0) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "playerId");
        }
        // end:玩家id,不能为空

        // begin:查询起终时间
        if (reqVo.getStartDate() != null && reqVo.getEndDate() != null) {
            if (reqVo.getStartDate().compareTo(reqVo.getEndDate()) > 0) {
                throw BusinessException.create(CommErrorEnum.START_TIME_IS_GREATER_THAN_END_TIME);
            }
        }
        // end:查询起终时间

        PagePackageRes<List<AgentSettleRecordDayDataRes>> data = this.service.dayData(pageReq, reqVo);
        return ResultAgent.success(data);
    }

    /**
     * 每日数据_游戏详情
     */
    @PostMapping("/dayDataDetails")
    public ResultAgent<PagePackageRes<List<AgentSettleRecordDayDataDetailsRes>>> dayDataDetails(@RequestBody PagePackageReq<AgentSettleRecordDayDataDetailsReq> req) {
        if (req == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "req");
        }
        PageReq pageReq = req.getPage();
        if (pageReq == null || PageReq.isError(pageReq)) {
            throw BusinessException.create(CommErrorEnum.PAGE_ERROR);
        }

        AgentSettleRecordDayDataDetailsReq reqVo = req.getParams();
        if (reqVo == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "params");
        }

        // begin:玩家id,不能为空
        if (reqVo.getPlayerId() <= 0) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "playerId");
        }
        // end:玩家id,不能为空

        // begin:游戏时间
        if (reqVo.getGameDate() == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "gameDate");
        }
        // end:游戏时间

        PagePackageRes<List<AgentSettleRecordDayDataDetailsRes>> data = this.service.dayDataDetails(pageReq, reqVo);
        return ResultAgent.success(data);
    }

    /**
     * 游戏详情
     */
    @PostMapping("/datailsCollect")
    public ResultAgent<PagePackageRes<List<AgentSettleRecordDatailsCollectRes>>> datailsCollect(@RequestBody PagePackageReq<AgentSettleRecordDatailsCollectReq> req) {
        if (req == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "req");
        }
        PageReq pageReq = req.getPage();
        if (pageReq == null || PageReq.isError(pageReq)) {
            throw BusinessException.create(CommErrorEnum.PAGE_ERROR);
        }

        AgentSettleRecordDatailsCollectReq reqVo = req.getParams();
        if (reqVo == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "params");
        }

        // begin:玩家id,不能为空
        if (reqVo.getPlayerId() <= 0) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "playerId");
        }
        // end:玩家id,不能为空

        PagePackageRes<List<AgentSettleRecordDatailsCollectRes>> data = this.service.datailsCollect(pageReq, reqVo);
        return ResultAgent.success(data);
    }

    /**
     * 游戏详情_每日数据
     */
    @PostMapping("/datailsCollectDayData")
    public ResultAgent<PagePackageRes<List<AgentSettleRecordDatailsCollectDayDataRes>>> datailsCollectDayData(@RequestBody PagePackageReq<AgentSettleRecordDatailsCollectDayDataReq> req) {
        if (req == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "req");
        }
        PageReq pageReq = req.getPage();
        if (pageReq == null || PageReq.isError(pageReq)) {
            throw BusinessException.create(CommErrorEnum.PAGE_ERROR);
        }

        AgentSettleRecordDatailsCollectDayDataReq reqVo = req.getParams();
        if (reqVo == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "params");
        }

        // begin:玩家id,不能为空
        if (reqVo.getPlayerId() <= 0) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "playerId");
        }
        // end:玩家id,不能为空

        // begin:游戏id,不能为空
        if (reqVo.getGameId() <= 0) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "gameId");
        }
        // end:游戏id,不能为空

        // begin:查询起终时间
        if (reqVo.getStartDate() != null && reqVo.getEndDate() != null) {
            if (reqVo.getStartDate().compareTo(reqVo.getEndDate()) > 0) {
                throw BusinessException.create(CommErrorEnum.START_TIME_IS_GREATER_THAN_END_TIME);
            }
        }
        // end:查询起终时间

        PagePackageRes<List<AgentSettleRecordDatailsCollectDayDataRes>> data = this.service.datailsCollectDayData(pageReq, reqVo);
        return ResultAgent.success(data);
    }
}