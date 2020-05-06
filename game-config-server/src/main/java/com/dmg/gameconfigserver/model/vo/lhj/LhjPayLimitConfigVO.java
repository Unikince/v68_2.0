package com.dmg.gameconfigserver.model.vo.lhj;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机派奖条件配置
 */
@Data
public class LhjPayLimitConfigVO {
	/**
	 * 序号
	 */
	@NotNull(message = "number不能为空")
	private int number;
	/**
	 * 奖池派发下限
	 */
	@NotNull(message = "payLowLimit不能为空")
	private double payLowLimit;
	/**
	 * 中奖概率
	 */
	@NotNull(message = "odds不能为空")
	private double odds;
	/**
	 * 派奖比例
	 */
	@NotNull(message = "payRatio不能为空")
	private double payRatio;

}
