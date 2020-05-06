package com.dmg.agentserver.business.controller;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserver.business.service.AgentRelationService;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
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
@RestController
@RequestMapping("/agent-api/agentRelation")
public class AgentRelationController {
    @Autowired
    private AgentRelationService service;

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        this.service.buildAgentRelation();
    }

    /**
     * 创建代理关系对象
     */
    @PostMapping(value = "/create")
    public ResultAgent<Void> create(@RequestBody AgentRelationCreateReq req) {
        if (req.getPlayerId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "playerId");
        }
        String nickName = req.getNickName();
        if (StringUtils.isBlank(nickName)) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "nickName");
        }
        this.service.create(req.getPlayerId(), nickName);
        return ResultAgent.success();
    }

    /**
     * 绑定代理关系
     */
    @PostMapping(value = "/bind")
    public ResultAgent<Void> bind(@RequestBody AgentRelationBindReq req) {
        if (req.getPlayerId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "playerId");
        }
        if (req.getParentId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "parentId");
        }
        this.service.bind(req.getPlayerId(), req.getParentId());
        return ResultAgent.success();
    }

    /**
     * 代理转移
     */
    @PostMapping(value = "/transfer")
    public ResultAgent<Void> transfer(@RequestBody AgentRelationTransferReq req) {
        if (req.getTransferId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "transferId");
        }
        if (req.getRecvId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "recvId");
        }
        this.service.transfer(req.getTransferId(), req.getRecvId(), req.isIncludeSelf());
        return ResultAgent.success();
    }

    /**
     * 后台玩家详情获取代理信息
     */
    @PostMapping(value = "/getInfoForAdminUserDetails")
    public ResultAgent<AgentRelationGifaudRes> getInfoForAdminUserDetails(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        AgentRelationGifaudRes obj = this.service.getInfoForAdminUserDetails(req.getId());
        return ResultAgent.success(obj);
    }

    /**
     * 客户端首页获取代理信息
     */
    @PostMapping(value = "/getInfoForClientIndex")
    public ResultAgent<AgentRelationGifciRes> getInfoForClientIndex(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        AgentRelationGifciRes obj = this.service.getInfoForClientIndex(req.getId());
        return ResultAgent.success(obj);
    }
}