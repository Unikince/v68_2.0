package com.zyhy.lhj_server.bgmanagement.entity;

import lombok.Data;

/**
 * 赔率奖池配置
 * @author Administrator
 *
 */
@Data
public class OddsPoolConfig {
	/**
	 * 奖池id
	 */
	private int PoolId;
	/**
	 * 奖池名称
	 */
	private String poolName;
	/**
	 * 累计比例
	 */
	private double poolTotalRatio;
	/**
	 * 当前奖池金额
	 */
	private double currentAmount;
	/**
	 * 当前状态
	 */
	private int state;
	/**
	 *  派奖次数
	 */
	private int payCount;
	/**
	 * 派奖金额
	 */
	private double payTotal;
	/**
	 * 平均派奖金额
	 */
	private double averageAmount;
	/**
	 * 最后派奖时间
	 */
	private String lastPayTime;
}
