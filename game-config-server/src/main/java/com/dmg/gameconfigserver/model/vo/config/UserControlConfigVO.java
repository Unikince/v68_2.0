package com.dmg.gameconfigserver.model.vo.config;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;

import lombok.Data;

/**
 * 用户控制配置
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
public class UserControlConfigVO {
	@NotNull(message = "id不能为空", groups = UpdateValid.class)
	private Integer id;
    /**
     * 流水最小值
     */
	@NotNull(message = "waterMinValue不能为空", groups = {UpdateValid.class, SaveValid.class})
    private BigDecimal waterMinValue;
    /**
     * 流水最大值
     */
	@NotNull(message = "waterMaxValue不能为空", groups = {UpdateValid.class, SaveValid.class})
    private BigDecimal waterMaxValue;
    /**
     * 预计消耗
     */
	@NotNull(message = "expectConsume不能为空", groups = {UpdateValid.class, SaveValid.class})
    private BigDecimal expectConsume;
    /**
     * 赢取奖励最大值
     */
	@NotNull(message = "rewardMaxValue不能为空", groups = {UpdateValid.class, SaveValid.class})
    private BigDecimal rewardMaxValue;
    /**
     * 自动控制模型
     */
	@NotNull(message = "autoControlModel不能为空", groups = {UpdateValid.class, SaveValid.class})
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