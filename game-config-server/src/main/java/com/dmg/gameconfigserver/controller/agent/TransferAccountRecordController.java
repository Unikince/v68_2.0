package com.dmg.gameconfigserver.controller.agent;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dmg.agentserviceapi.business.transferaccountrecord.feign.TransferAccountRecordFeign;
import com.dmg.agentserviceapi.business.transferaccountrecord.model.dto.TransferAccountRecordGetListRecv;
import com.dmg.agentserviceapi.business.transferaccountrecord.model.pojo.TransferAccountRecord;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ErrorResult;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.common.core.web.Result;
import com.dmg.gameconfigserver.common.constant.Constant;
import com.dmg.gameconfigserver.controller.BaseController;

/**
 * 转账纪录
 */
@RestController
@RequestMapping(Constant.CONTEXT_PATH + "agent/transferAccountRecord")
public class TransferAccountRecordController extends BaseController {
    @Autowired
    private TransferAccountRecordFeign feign;

    /**
     * 撤销转账
     */
    @PostMapping(value = "/cancel")
    @SuppressWarnings("rawtypes")
    public Result cancel(@RequestBody IdReq req) {
        ResultAgent<Void> result = this.feign.cancel(req);
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
    public Result getList(@RequestBody PagePackageReq<TransferAccountRecordGetListRecv> req) {
        ResultAgent<PagePackageRes<List<TransferAccountRecord>>> result = this.feign.getList(req);
        if (result.isSuccess()) {
            return Result.success(result.getData());
        } else {
            ErrorResult errorResult = result.getError();
            return Result.error("" + errorResult.getCode(), errorResult.getMsg());
        }
    }
}