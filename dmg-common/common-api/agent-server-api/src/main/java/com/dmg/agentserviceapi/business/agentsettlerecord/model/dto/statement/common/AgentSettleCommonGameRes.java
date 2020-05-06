package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代理结算_游戏_公用_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AgentSettleCommonGameRes extends AgentSettleCommonRes {
    /** 游戏id */
    private int gameId;
    /** 游戏名 */
    private String gameName;
}
