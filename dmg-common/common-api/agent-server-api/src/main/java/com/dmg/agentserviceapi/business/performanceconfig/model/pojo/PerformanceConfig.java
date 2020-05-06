package com.dmg.agentserviceapi.business.performanceconfig.model.pojo;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 业绩配置
 */
@Data
public class PerformanceConfig {
    /** 逻辑ID */
    private long id;
    /** 游戏id */
    private int gameId;
    /** 游戏名称 */
    private String gameName;
    /** 业绩比例(百分比) */
    private BigDecimal ratio;
}
