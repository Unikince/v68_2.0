package com.dmg.agentserviceapi.business.agentconfig.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dmg.agentserviceapi.business.agentconfig.model.pojo.AgentConfig;
import com.dmg.agentserviceapi.core.error.ResultAgent;

/**
 * 代理配置
 */
@FeignClient(value = "AGENT-SERVER", path = "/agent-api/agentConfig")
public interface AgentConfigFeign {
    /**
     * 修改
     */
    @PostMapping(value = "/update")
    ResultAgent<Void> update(@RequestBody AgentConfig obj);

    /**
     * 获取
     */
    @PostMapping(value = "/get")
    ResultAgent<AgentConfig> get();
}
