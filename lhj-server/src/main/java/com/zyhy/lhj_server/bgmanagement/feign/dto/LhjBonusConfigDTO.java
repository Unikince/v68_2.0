package com.zyhy.lhj_server.bgmanagement.feign.dto;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机彩金配置
 */
@Data
public class LhjBonusConfigDTO {
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
