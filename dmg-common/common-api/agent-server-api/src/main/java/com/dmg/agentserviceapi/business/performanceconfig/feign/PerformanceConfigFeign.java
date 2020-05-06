package com.dmg.agentserviceapi.business.performanceconfig.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dmg.agentserviceapi.business.performanceconfig.model.pojo.PerformanceConfig;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;

/**
 * 业绩配置
 */
@FeignClient(value = "AGENT-SERVER", path = "/agent-api/performanceConfig")
public interface PerformanceConfigFeign {
    /**
     * 修改
     *
     * @param id 逻辑ID
     * @param ratio 业绩比例(百分比)
     */
    @PostMapping(value = "/update")
    ResultAgent<Void> update(@RequestBody PerformanceConfig obj);

    /**
     * 获取所有对象
     */
    @PostMapping(value = "/getList")
    ResultAgent<PagePackageRes<List<PerformanceConfig>>> getList(@RequestBody PagePackageReq<Void> req);

}
