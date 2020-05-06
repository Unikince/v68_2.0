package com.dmg.agentserviceapi.business.transferaccountrecord.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dmg.agentserviceapi.business.transferaccountrecord.model.dto.TransferAccountRecordGetListRecv;
import com.dmg.agentserviceapi.business.transferaccountrecord.model.pojo.TransferAccountRecord;
import com.dmg.agentserviceapi.core.comm.IdReq;
import com.dmg.agentserviceapi.core.error.ResultAgent;
import com.dmg.agentserviceapi.core.pack.PagePackageReq;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;

/**
 * 转账纪录
 */
@FeignClient(value = "AGENT-SERVER", path = "/agent-api/transferAccountRecord")
public interface TransferAccountRecordFeign {

    /**
     * 创建转账纪录
     *
     * @param sourceId 转账玩家id
     * @param targetId 接收玩家id
     * @param sendAmount 转账金额
     */
    @PostMapping(value = "/create")
    public ResultAgent<Void> create(@RequestBody TransferAccountRecord obj);

    /**
     * 完成转账
     */
    @PostMapping(value = "/finish")
    ResultAgent<Void> finish(@RequestBody IdReq req);

    /**
     * 撤销转账
     */
    @PostMapping(value = "/cancel")
    ResultAgent<Void> cancel(@RequestBody IdReq req);

    /**
     * 获取所有对象
     */
    @PostMapping(value = "/getList")
    ResultAgent<PagePackageRes<List<TransferAccountRecord>>> getList(@RequestBody PagePackageReq<TransferAccountRecordGetListRecv> req);
}
