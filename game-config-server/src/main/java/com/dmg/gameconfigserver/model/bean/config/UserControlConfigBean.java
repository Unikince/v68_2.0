package com.dmg.gameconfigserver.model.bean.config;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户控制配置
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
@TableName("t_user_control_config")
public class UserControlConfigBean {
	@TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 流水最小值
     */
    private BigDecimal waterMinValue;
    /**
     * 流水最大值
     */
    private BigDecimal waterMaxValue;
    /**
     * 预计消耗
     */
    private BigDecimal expectConsume;
    /**
     * 赢取奖励最大值
     */
    private BigDecimal rewardMaxValue;
    /**
     * 自动控制模型
     */
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