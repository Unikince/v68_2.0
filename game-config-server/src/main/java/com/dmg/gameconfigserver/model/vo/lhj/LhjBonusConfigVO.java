package com.dmg.gameconfigserver.model.vo.lhj;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机彩金配置
 */
@Data
public class LhjBonusConfigVO {
	/**
	 * 游戏id
	 */
	@NotNull(message = "gameId不能为空")
	private int gameId;
	/**
	 * 游戏名字
	 */
	@org.hibernate.validator.constraints.NotEmpty(message = "gameName不能为空")
	private String gameName;
	/**
	 * 奖池名称
	 */
	@org.hibernate.validator.constraints.NotEmpty(message = "poolName不能为空")
	private String poolName;
	/**
	 * 初始化金额
	 */
	@NotNull(message = "initAmount不能为空")
	private double initAmount;
	/**
	 * 最低累计下注金额
	 */
	@NotNull(message = "LowBet不能为空")
	private double LowBet;
	/**
	 * 奖池累计比例
	 */
	@NotNull(message = "poolTotalRatio不能为空")
	private double poolTotalRatio;
	/**
	 * 奖池开启下限
	 */
	@NotNull(message = "poolOpenLow不能为空")
	private double poolOpenLow;
	/**
	 * 中奖概率
	 */
	@NotNull(message = "bonusLv不能为空")
	private double bonusLv;
	/**
	 * 中奖比例
	 */
	@NotNull(message = "rewardRatio不能为空")
	private double rewardRatio;

}
