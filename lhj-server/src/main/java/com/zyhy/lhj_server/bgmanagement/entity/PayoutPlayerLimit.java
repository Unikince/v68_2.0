package com.zyhy.lhj_server.bgmanagement.entity;

import lombok.Data;

/**
 * 玩家派奖条件
 * @author Administrator
 *
 */
@Data
public class PayoutPlayerLimit {
	/**
	 * 条件序号
	 */
	private int number;
	/**
	 * 累计输赢下限
	 */
	private double totalLowLimit;
	/**
	 * 当日输赢下限
	 */
	private double dayLowLimit;
	/**
	 * 累计流水下限
	 */
	private double totalWaterLow;
	/**
	 * 当日流水下限
	 */
	private double dayWaterLow;
	/**
	 * 累计赔率上限
	 */
	private double oddsTotalHight;
	/**
	 * 今日赔率上限
	 */
	private double dayOddsHight;
}
