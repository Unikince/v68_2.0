package com.dmg.agentserviceapi.business.agentrelation.model.dto;

import lombok.Data;

/**
 * 代理关系--代理转移--接收
 */
@Data
public class AgentRelationTransferReq {
    /** 转移代理id */
    private long transferId;
    /** 接收代理id */
    private long recvId;
    /** 是否包含自己 */
    private boolean includeSelf;
}
