package com.dmg.gameconfigserver.controller.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserviceapi.business.agentrelation.feign.AgentRelationFeign;
import com.dmg.agentserviceapi.business.agentrelation.model.dto.AgentRelationTransferReq;
import com.dmg.agentserviceapi.core.error.ErrorResult;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;

/**
 * 代理关系
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "agent/agentRelation")
public class AgentRelationController extends BaseController {
    @Autowired
    private AgentRelationFeign feign;

    /**
     * 代理转移
     */
    @PostMapping(value = "/transfer")
    @SuppressWarnings("rawtypes")
    public Result transfer(@RequestBody AgentRelationTransferReq req) {
        ResultAgent<Void> result = this.feign.transfer(req);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

}