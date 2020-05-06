package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.common;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代理结算_玩家_公用_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AgentSettleCommonPlayerRes extends AgentSettleCommonRes {
    /** 玩家id */
    private long userId;
    /** 玩家昵称 */
    private String userNick;
    /** 自得返佣 */
    private BigDecimal myBrokerage;
    /** 团队返佣 */
    private BigDecimal teamBrokerage;
}
