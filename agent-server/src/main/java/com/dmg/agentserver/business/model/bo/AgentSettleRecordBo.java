package com.dmg.agentserver.business.model.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 代理结算-纪录
 */
@Data
public class AgentSettleRecordBo {
    /** 逻辑键 */
    private long id;
    /** 日期 */
    private Date dayStr;
    /** 玩家id */
    private long userId;
    /** 玩家昵称 */
    private String userNick;
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
    /** 历史返佣 */
    private BigDecimal historyBrokerage;
}
