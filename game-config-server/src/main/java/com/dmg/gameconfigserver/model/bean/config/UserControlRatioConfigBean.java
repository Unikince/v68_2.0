package com.dmg.gameconfigserver.model.bean.config;

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
@TableName("t_user_control_ratio_config")
public class UserControlRatioConfigBean {
	@TableId(type = IdType.AUTO)
    private Integer id;
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