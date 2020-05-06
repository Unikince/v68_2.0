package com.dmg.agentserver.business.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.agentserviceapi.business.performanceconfig.model.pojo.PerformanceConfig;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业绩配置
 */
@Data
@TableName("a_performance_config")
@EqualsAndHashCode(callSuper = false)
public class PerformanceConfigPo extends PerformanceConfig {
}
