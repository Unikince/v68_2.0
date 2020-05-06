package com.dmg.gameconfigserver.model.vo.config;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户控制比例配置
 **/
@Data
public class UserControlRatioConfigVO {
    /**
     * 基础返奖率
     */
	@NotNull(message = "baseRewardRate不能为空")
    private BigDecimal baseRewardRate;
    /**
     * 自动控制阀值
     */
	@NotNull(message = "autoControlValue不能为空")
    private BigDecimal autoControlValue;
    /**
     * 流水倍率
     */
    private BigDecimal waterRatio;
}