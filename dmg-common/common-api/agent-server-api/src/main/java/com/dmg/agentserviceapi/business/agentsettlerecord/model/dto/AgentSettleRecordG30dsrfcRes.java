package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 代理结算纪录--客户端获取30天结算纪录--返回
 */
@Data
public class AgentSettleRecordG30dsrfcRes {
    /** 日期 */
    private Date dayStr;
    /** 直属下级业绩 */
    private BigDecimal subDirectlyPerformance;
    /** 下级团队业绩 */
    private BigDecimal subTeamPerformance;
    /** 结算佣金 */
    private BigDecimal myBrokerage;
    /** 历史佣金 */
    private BigDecimal historyBrokerage;

}
