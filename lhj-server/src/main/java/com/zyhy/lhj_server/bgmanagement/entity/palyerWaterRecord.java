package com.zyhy.lhj_server.bgmanagement.entity;

/**
 * 玩家流水记录
 * @author Administrator
 *
 */
public class palyerWaterRecord {
	private int id;
	private String roleid;
	private String rolenick  ;
	private String uuid;
	// 今日下注
	private double todayBet;
	// 今日派彩
	private double todayreward;
	// 今日收益
	private double todayWin;
	// 今日收益率
	private double todayWinlv;
	// 累计下注
	private double totalBet;
	// 累计派彩
	private double totalreward;
	// 累计收益
	private double totalWin;
	// 累计收益率
	private double totalWinlv;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolenick() {
		return rolenick;
	}

	public void setRolenick(String rolenick) {
		this.rolenick = rolenick;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public double getTodayWinlv() {
		return todayWinlv;
	}

	public void setTodayWinlv(double todayWinlv) {
		this.todayWinlv = todayWinlv;
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

	public double getTotalWinlv() {
		return totalWinlv;
	}

	public void setTotalWinlv(double totalWinlv) {
		this.totalWinlv = totalWinlv;
	}

	@Override
	public String toString() {
		return "palyerWaterRecord [id=" + id + ", roleid=" + roleid + ", rolenick=" + rolenick + ", uuid=" + uuid
				+ ", todayBet=" + todayBet + ", todayreward=" + todayreward + ", todayWin=" + todayWin + ", todayWinlv="
				+ todayWinlv + ", totalBet=" + totalBet + ", totalreward=" + totalreward + ", totalWin=" + totalWin
				+ ", totalWinlv=" + totalWinlv + "]";
	}
	
}
