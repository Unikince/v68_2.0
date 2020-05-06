package com.dmg.agentserviceapi.business.agentlevel.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dmg.agentserviceapi.business.agentlevel.model.pojo.AgentLevel;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;

/**
 * 代理等级
 */
@FeignClient(value = "AGENT-SERVER", path = "/agent-api/agentLevel")
public interface AgentLevelFeign {
    /**
     * 添加
     *
     * @param name 等级名称
     * @param performanceBegin 日业绩起点(包含)
     * @param performanceEnd 日业绩终点(不包含)
     * @param brokerageRatio 佣金比例(百分比)
     */
    @PostMapping(value = "/add")
    ResultAgent<Void> add(@RequestBody AgentLevel obj);

    /**
     * 修改
     *
     * @param id 逻辑ID
     * @param performanceBegin 日业绩起点(包含)
     * @param performanceEnd 日业绩终点(不包含)
     * @param brokerageRatio 佣金比例(百分比)
     */
    @PostMapping(value = "/update")
    ResultAgent<Void> update(@RequestBody AgentLevel obj);

    /**
     * 删除
     */
    @PostMapping(value = "/delete")
    ResultAgent<Void> delete(@RequestBody IdReq req);

    /**
     * 获取所有对象
     * 
     * @param page 分页数据
     */
    @PostMapping(value = "/getList")
    ResultAgent<PagePackageRes<List<AgentLevel>>> getList(@RequestBody PagePackageReq<Void> req);
}
