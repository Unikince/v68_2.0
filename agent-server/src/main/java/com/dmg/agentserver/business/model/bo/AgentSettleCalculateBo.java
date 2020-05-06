package com.dmg.agentserver.business.model.bo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理结算计算中间对象
 */
@Data
public class AgentSettleCalculateBo {
    /** 逻辑键 */
    private long id;
    /** 玩家id */
    private long userId;
    /** 玩家昵称 */
    private String userNick;
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
    /** 代理等级id */
    private long agentLevelId;
    /** 自得返佣 */
    private BigDecimal myBrokerage;
    /** 团队返佣 */
    private BigDecimal teamBrokerage;
}
