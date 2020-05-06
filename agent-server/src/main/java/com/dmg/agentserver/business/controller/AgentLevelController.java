package com.dmg.agentserver.business.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserver.business.service.AgentLevelService;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
import com.dmg.agentserviceapi.business.agentlevel.model.pojo.AgentLevel;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.agentserviceapi.core.page.PageReq;

/**
 * 代理等级
 */
@RestController
@RequestMapping("/agent-api/agentLevel")
public class AgentLevelController {
    @Autowired
    private AgentLevelService service;

    /**
     * 添加
     */
    @PostMapping(value = "/add")
    public ResultAgent<Void> add(@RequestBody AgentLevel obj) {
        this.checkParamsOfAddAndUpdate(obj, 1);
        this.service.add(obj);
        return ResultAgent.success();
    }

    /**
     * 修改
     */
    @PostMapping(value = "/update")
    public ResultAgent<Void> update(@RequestBody AgentLevel obj) {
        this.checkParamsOfAddAndUpdate(obj, 2);
        this.service.update(obj);
        return ResultAgent.success();
    }

    /**
     * 删除
     */
    @PostMapping(value = "/delete")
    public ResultAgent<Void> delete(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        this.service.delete(req.getId());
        return ResultAgent.success();
    }

    /**
     * 获取所有对象
     */
    @PostMapping(value = "/getList")
    public ResultAgent<PagePackageRes<List<AgentLevel>>> getList(@RequestBody PagePackageReq<Void> req) {
        if (req == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "req");
        }
        PageReq pageReq = req.getPage();
        if (pageReq == null || PageReq.isError(pageReq)) {
            throw BusinessException.create(CommErrorEnum.PAGE_ERROR);
        }

        PagePackageRes<List<AgentLevel>> pagePackageRes = this.service.getList(pageReq);
        return ResultAgent.success(pagePackageRes);
    }

    /**
     * 检查添加和修改的方法的参数
     *
     * @param action 动作(1添加，2修改)
     */
    private void checkParamsOfAddAndUpdate(AgentLevel obj, int action) {
        if (action == 2) {
            {// begin:ID
                long id = obj.getId();
                if (id <= 0) {
                    throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
                }
            } // end:ID
        }
        {// begin:等级名称
            String name = obj.getName();
            if (StringUtils.isBlank(name)) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "name");
            }
            if (name.length() > 6) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "name:" + name.length());
            }
        } // end:等级名称

        {// begin:日业绩起点(包含)
            BigDecimal performanceBegin = obj.getPerformanceBegin();
            if (performanceBegin == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "performanceBegin");
            }
            if (performanceBegin.compareTo(BigDecimal.ZERO) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_ZERO, "performanceBegin=" + performanceBegin);
            }
            String performanceBeginStr = performanceBegin.toString();
            if (performanceBeginStr.length() > 20) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "performanceBegin:" + performanceBeginStr.length());
            }
            obj.setPerformanceBegin(performanceBegin.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:日业绩起点(包含)

        {// begin:日业绩终点(不包含)
            BigDecimal performanceEnd = obj.getPerformanceEnd();
            if (performanceEnd == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "performanceEnd");
            }
            if (performanceEnd.compareTo(BigDecimal.ZERO) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_ZERO, "performanceEnd=" + performanceEnd);
            }
            if (performanceEnd.compareTo(obj.getPerformanceBegin()) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_MIN, "performanceEnd<" + obj.getPerformanceBegin());
            }
            String performanceEndStr = performanceEnd.toString();
            if (performanceEndStr.length() > 20) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "performanceEnd:" + performanceEndStr.length());
            }
            obj.setPerformanceEnd(performanceEnd.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:日业绩终点(不包含)

        {// begin:佣金比例(百分比)
            BigDecimal brokerageRatio = obj.getBrokerageRatio();
            if (brokerageRatio == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "brokerageRatio");
            }
            if (brokerageRatio.compareTo(BigDecimal.ZERO) < 0) {
                throw BusinessException.create(CommErrorEnum.LT_ZERO, "brokerageRatio=" + brokerageRatio);
            }
            String brokerageRatioStr = brokerageRatio.toString();
            if (brokerageRatioStr.length() > 6) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "brokerageRatio:" + brokerageRatioStr.length());
            }
            obj.setBrokerageRatio(brokerageRatio.setScale(2, BigDecimal.ROUND_HALF_UP));
        } // end:佣金比例(百分比)
    }
}