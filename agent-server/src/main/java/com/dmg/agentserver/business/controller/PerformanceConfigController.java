package com.dmg.agentserver.business.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserver.business.service.PerformanceConfigService;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
import com.dmg.agentserviceapi.business.performanceconfig.model.pojo.PerformanceConfig;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.agentserviceapi.core.page.PageReq;

/**
 * 业绩配置
 */
@RestController
@RequestMapping("/agent-api/performanceConfig")
public class PerformanceConfigController {
    @Autowired
    private PerformanceConfigService service;

    /**
     * 修改
     */
    @PostMapping(value = "/update")
    public ResultAgent<Void> update(@RequestBody PerformanceConfig obj) {
        {// begin:ID
            long id = obj.getId();
            if (id <= 0) {
                throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
            }
        } // end:ID

        {// begin:业绩比例(百分比)
            BigDecimal ratio = obj.getRatio();
            if (ratio == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "ratio");
            }
            String ratioStr = ratio.toString();
            if (ratioStr.length() > 6) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "ratio:" + ratioStr.length());
            }
            obj.setRatio(ratio.setScale(0, BigDecimal.ROUND_HALF_UP));
        } // end:业绩比例(百分比)
        this.service.update(obj);
        return ResultAgent.success();
    }

    /**
     * 获取所有对象
     */
    @PostMapping(value = "/getList")
    public ResultAgent<PagePackageRes<List<PerformanceConfig>>> getList(@RequestBody PagePackageReq<Void> req) {
        if (req == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "req");
        }
        PageReq pageReq = req.getPage();
        if (pageReq == null || PageReq.isError(pageReq)) {
            throw BusinessException.create(CommErrorEnum.PAGE_ERROR);
        }
        PagePackageRes<List<PerformanceConfig>> objs = this.service.getList(pageReq);
        return ResultAgent.success(objs);
    }
}