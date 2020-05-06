package com.dmg.agentserver.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserver.business.service.AgentSettleService;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordG30dsrfcRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifaudRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGifciRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.AgentSettleRecordGysfcRes;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ResultAgent;

/**
 * 代理结算
 */
@RestController
@RequestMapping("/agent-api/agentSettle")
public class AgentSettleController {
    @Autowired
    private AgentSettleService service;

    /**
     * 后台玩家详情获取结算信息
     */
    @PostMapping(value = "/getInfoForAdminUserDetails")
    public ResultAgent<AgentSettleRecordGifaudRes> getInfoForAdminUserDetails(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        AgentSettleRecordGifaudRes obj = this.service.getInfoForAdminUserDetails(req.getId());
        return ResultAgent.success(obj);
    }

    /**
     * 客户端首页获取结算信息
     */
    @PostMapping(value = "/getInfoForClientIndex")
    public ResultAgent<AgentSettleRecordGifciRes> getInfoForClientIndex(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        AgentSettleRecordGifciRes obj = this.service.getInfoForClientIndex(req.getId());
        return ResultAgent.success(obj);
    }

    /**
     * 客户端获取昨日结算
     */
    @PostMapping(value = "/getYestorySettleForClient")
    public ResultAgent<AgentSettleRecordGysfcRes> getYestorySettleForClient(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        AgentSettleRecordGysfcRes obj = this.service.getYestorySettleForClient(req.getId());
        return ResultAgent.success(obj);
    }

    /**
     * 客户端获取30天结算纪录
     */
    @PostMapping(value = "/get30DaySettleRecordForClient")
    public ResultAgent<AgentSettleRecordG30dsrfcRes> get30DaySettleRecordForClient(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        AgentSettleRecordG30dsrfcRes obj = this.service.get30DaySettleRecordForClient(req.getId());
        return ResultAgent.success(obj);
    }

}