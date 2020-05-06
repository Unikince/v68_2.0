package com.dmg.gameconfigserver.model.dto.config;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.annotation.ColumnName;

import lombok.Data;

/**
 * 用户控制比例配置
 **/
@Data
@ColumnName(name = "用户控制比例配置")
public class UserControlRatioConfigDTO {
    /**
     * 基础返奖率
     */
	@ColumnName(name = "基础返奖率")
    private BigDecimal baseRewardRate;
    /**
     * 自动控制阀值
     */
	@ColumnName(name = "自动控制阀值")
    private BigDecimal autoControlValue;
    /**
     * 流水倍率
     */
    private BigDecimal waterRatio;
}