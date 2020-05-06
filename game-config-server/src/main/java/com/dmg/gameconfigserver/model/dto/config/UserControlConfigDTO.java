package com.dmg.gameconfigserver.model.dto.config;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.annotation.ColumnName;

import lombok.Data;

/**
 * 用户控制配置
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
@ColumnName(name = "用户控制配置")
public class UserControlConfigDTO {
	 private Integer id;
    /**
     * 流水最小值
     */
	 @ColumnName(name = "流水最小值")
    private BigDecimal waterMinValue;
    /**
     * 流水最大值
     */
	 @ColumnName(name = "流水最大值")
    private BigDecimal waterMaxValue;
    /**
     * 预计消耗
     */
    private BigDecimal expectConsume;
    /**
     * 赢取奖励最大值
     */
    @ColumnName(name = "赢取奖励最大值")
    private BigDecimal rewardMaxValue;
    /**
     * 自动控制模型
     */
    @ColumnName(name = "自动控制模型")
    private Integer autoControlModel;
    /**
     * 基础返奖率
     */
    private BigDecimal baseRewardRate;
    /**
     * 自动控制阀值
     */
    private BigDecimal autoControlValue;
    /**
     * 流水倍率
     */
    private BigDecimal waterRatio;
}