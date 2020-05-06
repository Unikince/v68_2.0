package com.dmg.gameconfigserver.controller.agent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserviceapi.business.performanceconfig.feign.PerformanceConfigFeign;
import com.dmg.agentserviceapi.business.performanceconfig.model.pojo.PerformanceConfig;
import com.dmg.agentserviceapi.core.error.ErrorResult;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;

/**
 * 业绩配置
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "agent/performanceConfig")
public class PerformanceConfigController extends BaseController {
    @Autowired
    private PerformanceConfigFeign feign;

    /**
     * 修改
     */
    @PostMapping(value = "/update")
    @SuppressWarnings("rawtypes")
    public Result update(@RequestBody PerformanceConfig obj) {
        ResultAgent<Void> result = this.feign.update(obj);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }

    /**
     * 获取所有对象
     */
    @PostMapping(value = "/getList")
    @SuppressWarnings("rawtypes")
    public Result getList(@RequestBody PagePackageReq<Void> req) {
        ResultAgent<PagePackageRes<List<PerformanceConfig>>> result = this.feign.getList(req);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }
}