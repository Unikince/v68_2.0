package com.zyhy.lhj_server.bgmanagement.entity;

import lombok.Data;

/**
 * 派奖条件
 * @author Administrator
 *
 */
@Data
public class PayoutLimit {
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
