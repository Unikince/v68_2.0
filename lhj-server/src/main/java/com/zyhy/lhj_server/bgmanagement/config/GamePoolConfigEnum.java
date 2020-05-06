package com.zyhy.lhj_server.bgmanagement.config;

public enum GamePoolConfigEnum {
	Grand(0, null, "grand",10000, 10, 0.04, 100000, 0.01, 1),
	Major(0, null, "major",5000, 10, 0.03, 50000, 0.01, 1),
	Minor(0, null, "minor",100, 10, 0.02, 10000, 0.01, 1),
	Mini(0, null, "mini",10, 10, 0.01, 1000, 0.01, 1)
	;
	// 游戏id
	private int gameId;
	// 游戏名字
	private String gameName;
	// 奖池名称
	private String poolName;
	// 初始化金额
	private double initAmount;
	// 最低累计下注金额
	private double LowBet;
	// 奖池累计比例
	private double poolTotalRatio;
	// 奖池开启下限
	private double poolOpenLow;
	// 中奖概率
	private double bonusLv;
	// 中奖比例
	private double rewardRatio;
	private GamePoolConfigEnum(int gameId, String gameName, String poolName, double initAmount, double lowBet,
			double poolTotalRatio, double poolOpenLow, double bonusLv, double rewardRatio) {
		this.gameId = gameId;
		this.gameName = gameName;
		this.poolName = poolName;
		this.initAmount = initAmount;
		LowBet = lowBet;
		this.poolTotalRatio = poolTotalRatio;
		this.poolOpenLow = poolOpenLow;
		this.bonusLv = bonusLv;
		this.rewardRatio = rewardRatio;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public double getInitAmount() {
		return initAmount;
	}
	public void setInitAmount(double initAmount) {
		this.initAmount = initAmount;
	}
	public double getLowBet() {
		return LowBet;
	}
	public void setLowBet(double lowBet) {
		LowBet = lowBet;
	}
	public double getPoolTotalRatio() {
		return poolTotalRatio;
	}
	public void setPoolTotalRatio(double poolTotalRatio) {
		this.poolTotalRatio = poolTotalRatio;
	}
	public double getPoolOpenLow() {
		return poolOpenLow;
	}
	public void setPoolOpenLow(double poolOpenLow) {
		this.poolOpenLow = poolOpenLow;
	}
	public double getBonusLv() {
		return bonusLv;
	}
	public void setBonusLv(double bonusLv) {
		this.bonusLv = bonusLv;
	}
	public double getRewardRatio() {
		return rewardRatio;
	}
	public void setRewardRatio(double rewardRatio) {
		this.rewardRatio = rewardRatio;
	}
}
