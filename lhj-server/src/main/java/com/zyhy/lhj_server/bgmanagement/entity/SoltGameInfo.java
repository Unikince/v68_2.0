package com.zyhy.lhj_server.bgmanagement.entity;

import lombok.Data;

/**
 * 老虎机游戏信息
 * @author Administrator
 *
 */
@Data
public class SoltGameInfo {
	/**
	 * 游戏名称
	 */
	private String gameName;
	/**
	 * redis名称
	 */
	private String redisName;
	/**
	 * 游戏id
	 */
	private int gameId;
	/**
	 * 序号
	 */
	private int number;
	/**
	 * 准入金额
	 */
	private double inAmount;
	/**
	 * 当前状态
	 */
	private int state;
	/**
	 * 当前玩家数量
	 */
	private int playerNumber;
	/**
	 * 下注列表
	 */
	private String betList;
	/**
	 * 总下注
	 */
	private double totalBet;
	/**
	 * 总赔付
	 */
	private double totalPay;
	/**
	 * 当前库存
	 */
	private double inventory;
	/**
	 * 设置库存值(正数:系统赢,负数:系统输)
	 */
	private double setInventory;
	
	/**
	 * 当前输赢值
	 */
	private double currentWinLoseValue;
	/**
	 * 当前赔率
	 */
	private double Odds;
	/**
	 * 人数上限
	 */
	private int totalPlayerNumber;
	/**
	 * 赢家抽水
	 */
	private int winReward;
	/**
	 * 大额赔付验证金额
	 */
	private double checkAmount;
	/**
	 * 大额赔付验证赔率
	 */
	private double checkOdds;
	/**
	 * 大奖几率
	 */
	private double bigRewardOdds;
}
