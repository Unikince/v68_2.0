package com.dmg.gameconfigserver.model.vo.lhj;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机场次配置
 */
@Data
public class LhjFieldConfigVO {
	/**
	 * 游戏名称
	 */
	@org.hibernate.validator.constraints.NotEmpty(message = "gameName不能为空")
	private String gameName;
	/**
	 * redis名称
	 */
	@org.hibernate.validator.constraints.NotEmpty(message = "redisName不能为空")
	private String redisName;
	/**
	 * 游戏id
	 */
	@NotNull(message = "gameId不能为空")
	private int gameId;
	/**
	 * 序号
	 */
	@NotNull(message = "number不能为空")
	private int number;
	/**
	 * 准入金额
	 */
	@NotNull(message = "inAmount不能为空")
	private double inAmount;
	/**
	 * 当前状态
	 */
	@NotNull(message = "state不能为空")
	private int state;
	/**
	 * 当前玩家数量
	 */
	private int playerNumber;
	/**
	 * 下注列表
	 */
	@org.hibernate.validator.constraints.NotEmpty(message = "betList不能为空")
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
	@NotNull(message = "checkAmount不能为空")
	private double checkAmount;
	/**
	 * 大额赔付验证赔率
	 */
	@NotNull(message = "checkOdds不能为空")
	private double checkOdds;
	/**
	 * 大奖几率
	 */
	@NotNull(message = "bigRewardOdds不能为空")
	private double bigRewardOdds;
}
