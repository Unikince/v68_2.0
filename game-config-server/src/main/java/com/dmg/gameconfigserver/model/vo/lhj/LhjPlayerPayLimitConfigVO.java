package com.dmg.gameconfigserver.model.vo.lhj;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机玩家派奖条件配置
 */
@Data
public class LhjPlayerPayLimitConfigVO {
	/**
	 * 条件序号
	 */
	@NotNull(message = "number不能为空")
	private int number;
	/**
	 * 累计输赢下限
	 */
	@NotNull(message = "totalLowLimit不能为空")
	private double totalLowLimit;
	/**
	 * 当日输赢下限
	 */
	@NotNull(message = "dayLowLimit不能为空")
	private double dayLowLimit;
	/**
	 * 累计流水下限
	 */
	@NotNull(message = "totalWaterLow不能为空")
	private double totalWaterLow;
	/**
	 * 当日流水下限
	 */
	@NotNull(message = "dayWaterLow不能为空")
	private double dayWaterLow;
	/**
	 * 累计赔率上限
	 */
	@NotNull(message = "oddsTotalHight不能为空")
	private double oddsTotalHight;
	/**
	 * 今日赔率上限
	 */
	@NotNull(message = "dayOddsHight不能为空")
	private double dayOddsHight;

}
