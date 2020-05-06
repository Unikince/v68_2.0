package com.dmg.gameconfigserver.controller.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserviceapi.business.agentconfig.feign.AgentConfigFeign;
import com.dmg.agentserviceapi.business.agentconfig.model.pojo.AgentConfig;
import com.dmg.agentserviceapi.core.error.ErrorResult;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;

/**
 * 代理配置
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "agent/agentConfig")
public class AgentConfigController extends BaseController {
    @Autowired
    private AgentConfigFeign feign;

    /**
     * 修改
     */
    @PostMapping(value = "/update")
    @SuppressWarnings("rawtypes")
    public Result update(@RequestBody AgentConfig obj) {
        ResultAgent<Void> result = this.feign.update(obj);
        if (result.isSuccess()) {
            return Result.success();
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

    /**
     * 获取
     */
    @PostMapping(value = "/get")
    @SuppressWarnings("rawtypes")
    public Result get() {
        ResultAgent<AgentConfig> result = this.feign.get();
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

}