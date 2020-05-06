package com.zyhy.lhj_server.bgmanagement.entity;

/**
 * 总流水记录
 * @author Administrator
 *
 */
public class totalWaterRecord {
	// 表名
	private String tableName;
	// 今日下注
	private double todayBet;
	// 今日派彩
	private double todayreward;
	// 今日收益
	private double todayWin;
	// 累计下注
	private double totalBet;
	// 累计派彩
	private double totalreward;
	// 累计收益
	private double totalWin;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public double getTodayBet() {
		return todayBet;
	}

	public void setTodayBet(double todayBet) {
		this.todayBet = todayBet;
	}

	public double getTodayreward() {
		return todayreward;
	}

	public void setTodayreward(double todayreward) {
		this.todayreward = todayreward;
	}

	public double getTodayWin() {
		return todayWin;
	}

	public void setTodayWin(double todayWin) {
		this.todayWin = todayWin;
	}

	public double getTotalBet() {
		return totalBet;
	}

	public void setTotalBet(double totalBet) {
		this.totalBet = totalBet;
	}

	public double getTotalreward() {
		return totalreward;
	}

	public void setTotalreward(double totalreward) {
		this.totalreward = totalreward;
	}

	public double getTotalWin() {
		return totalWin;
	}

	public void setTotalWin(double totalWin) {
		this.totalWin = totalWin;
	}

	@Override
	public String toString() {
		return "totalWaterRecord [tableName=" + tableName + ", todayBet=" + todayBet + ", todayreward=" + todayreward
				+ ", todayWin=" + todayWin + ", totalBet=" + totalBet + ", totalreward=" + totalreward + ", totalWin="
				+ totalWin + "]";
	}
	
}
