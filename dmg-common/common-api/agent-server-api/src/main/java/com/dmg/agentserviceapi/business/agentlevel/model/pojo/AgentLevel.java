package com.dmg.agentserviceapi.business.agentlevel.model.pojo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 代理等级
 */
@Data
public class AgentLevel {
    /** 逻辑ID */
    private long id;
    /** 序号(等级) */
    private int sort;
    /** 等级名称 */
    private String name;
    /** 日业绩起点(包含) */
    private BigDecimal performanceBegin;
    /** 日业绩终点(不包含) */
    private BigDecimal performanceEnd;
    /** 佣金比例(百分比) */
    private BigDecimal brokerageRatio;
}
