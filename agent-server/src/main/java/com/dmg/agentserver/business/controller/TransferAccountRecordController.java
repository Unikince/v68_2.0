package com.dmg.agentserver.business.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserver.business.service.TransferAccountRecordService;
import com.dmg.agentserver.core.exception.BusinessException;
import com.dmg.agentserver.core.exception.CommErrorEnum;
import com.dmg.agentserviceapi.business.transferaccountrecord.model.dto.TransferAccountRecordGetListRecv;
import com.dmg.agentserviceapi.business.transferaccountrecord.model.pojo.TransferAccountRecord;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.agentserviceapi.core.page.PageReq;

/**
 * 转账纪录
 */
@RestController
@RequestMapping("/agent-api/transferAccountRecord")
public class TransferAccountRecordController {
    @Autowired
    private TransferAccountRecordService service;

    /**
     * 创建转账
     */
    @PostMapping(value = "/create")
    public ResultAgent<Void> create(@RequestBody TransferAccountRecord obj) {
        {// begin:转账玩家id
            long sourceId = obj.getSourceId();
            if (sourceId <= 0) {
                throw BusinessException.create(CommErrorEnum.LE_ZERO, "sourceId");
            }
        } // end:转账玩家id

        {// begin:转账玩家id
            long targetId = obj.getTargetId();
            if (targetId <= 0) {
                throw BusinessException.create(CommErrorEnum.LE_ZERO, "targetId");
            }
        } // end:转账玩家id

        {// begin:转账金额
            BigDecimal sendAmount = obj.getSendAmount();
            if (sendAmount == null) {
                throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "sendAmount");
            }
            if (sendAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw BusinessException.create(CommErrorEnum.PARAM_ERROR, "sendAmount=" + sendAmount);
            }
            String sendAmountStr = sendAmount.toString();
            if (sendAmountStr.length() > 20) {
                throw BusinessException.create(CommErrorEnum.LENGTH_ERROR, "sendAmount:" + sendAmountStr.length());
            }
            obj.setSendAmount(sendAmount.setScale(0, BigDecimal.ROUND_HALF_UP));
        } // end:转账金额

        this.service.create(obj);
        return ResultAgent.success();
    }

    /**
     * 完成转账
     */
    @PostMapping(value = "/finish")
    public ResultAgent<Void> finish(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        this.service.finish(req.getId());
        return ResultAgent.success();
    }

    /**
     * 撤销转账
     */
    @PostMapping(value = "/cancel")
    public ResultAgent<Void> cancel(@RequestBody IdReq req) {
        if (req.getId() <= 0) {
            throw BusinessException.create(CommErrorEnum.LE_ZERO, "id");
        }
        this.service.cancel(req.getId());
        return ResultAgent.success();
    }

    /**
     * 获取所有对象
     */
    @PostMapping(value = "/getList")
    public ResultAgent<PagePackageRes<List<TransferAccountRecord>>> getList(@RequestBody PagePackageReq<TransferAccountRecordGetListRecv> req) {
        if (req == null) {
            throw BusinessException.create(CommErrorEnum.PARAM_EMPTY, "req");
        }
        PageReq pageReq = req.getPage();
        if (pageReq == null || PageReq.isError(pageReq)) {
            throw BusinessException.create(CommErrorEnum.PAGE_ERROR);
        }

        TransferAccountRecordGetListRecv reqVo = req.getParams();
        if (reqVo == null) {
            reqVo = new TransferAccountRecordGetListRecv();
        }

        // begin:转账玩家id
        if (reqVo.getSourceId() < 0) {
            throw BusinessException.create(CommErrorEnum.LT_ZERO, "sourceId=" + reqVo.getSourceId());
        }
        // end:转账玩家id

        // begin: 接收玩家id
        if (reqVo.getTargetId() < 0) {
            throw BusinessException.create(CommErrorEnum.LT_ZERO, "targetId" + reqVo.getTargetId());
        }
        // end: 接收玩家id

        // begin:状态(0,所有,1开始,2完成,3撤回)
        if (reqVo.getStatus() < 0 || reqVo.getStatus() > 3) {
            throw BusinessException.create(CommErrorEnum.PARAM_ERROR, "status=" + reqVo.getStatus());
        }
        // end:状态(0,所有,1开始,2完成,3撤回)

        // begin:查询起终时间
        if (reqVo.getStartDate() != null && reqVo.getEndDate() != null) {
            if (reqVo.getStartDate().compareTo(reqVo.getEndDate()) > 0) {
                throw BusinessException.create(CommErrorEnum.START_TIME_IS_GREATER_THAN_END_TIME);
            }
        }
        // end:查询起终时间

        PagePackageRes<List<TransferAccountRecord>> objs = this.service.getList(pageReq, reqVo);
        return ResultAgent.success(objs);
    }
}