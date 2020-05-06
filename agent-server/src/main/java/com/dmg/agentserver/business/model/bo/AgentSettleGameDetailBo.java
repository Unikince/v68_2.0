package com.dmg.agentserver.business.model.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理结算-游戏详情
 */
@Data
public class AgentSettleGameDetailBo {
    /** 逻辑键 */
    private long id;
    /** 结算纪录id */
    private long recordId;
    /** 游戏id */
    private int gameId;
    /** 游戏名 */
    private String gameName;
    /** 自营业绩 */
    private BigDecimal myPerformance;
    /** 直属下级业绩 */
    private BigDecimal subDirectlyPerformance;
    /** 下级团队业绩 */
    private BigDecimal subTeamPerformance;
}
