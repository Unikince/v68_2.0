package com.dmg.gameconfigserver.model.dto.lhj;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机派奖条件配置
 */
@Data
public class LhjPayLimitConfigDTO {
	/**
	 * 序号
	 */
	private int number;
	/**
	 * 奖池派发下限
	 */
	private double payLowLimit;
	/**
	 * 中奖概率
	 */
	private double odds;
	/**
	 * 派奖比例
	 */
	private double payRatio;

}
