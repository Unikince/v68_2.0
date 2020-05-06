package com.zyhy.lhj_server.bgmanagement.entity;

import lombok.Data;

/**
 * 年年有余,糖果派对奖池配置
 * @author Administrator
 *
 */
@Data
public class GamePoolConfig {
	/**
	 * 游戏id
	 */
	private int gameId;
	/**
	 * 游戏名字
	 */
	private String gameName;
	/**
	 * 奖池名称
	 */
	private String poolName;
	/**
	 * 初始化金额
	 */
	private double initAmount;
	/**
	 * 最低累计下注金额
	 */
	private double LowBet;
	/**
	 * 奖池累计比例
	 */
	private double poolTotalRatio;
	/**
	 * 奖池开启下限
	 */
	private double poolOpenLow;
	/**
	 * 中奖概率
	 */
	private double bonusLv;
	/**
	 * 中奖比例
	 */
	private double rewardRatio;
}
