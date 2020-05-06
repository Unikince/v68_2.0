package com.dmg.bcbm.logic.entity.bcbm;

import java.util.Arrays;
import java.util.Map;

public class SingleBetInfo {
	// 玩家id
	private String roleid;
	// 下注类型
	private int car;
	// 下注金额
	private double bet;
	// 下注级别
	private int betLevel;
	// 当前总投注
	private double totalBet;
	// 投注玩家当前的投注详情
	private double[] playerBetInfo;
	// 每个车当前的总投注
	private double[] carBet = new double[8];
	public SingleBetInfo(String roleid, int car, double bet) {
		super();
		this.roleid = roleid;
		this.car = car;
		this.bet = bet;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public int getCar() {
		return car;
	}
	public void setCar(int car) {
		this.car = car;
	}
	public double getBet() {
		return bet;
	}
	public void setBet(double bet) {
		this.bet = bet;
	}
	public double getTotalBet() {
		return totalBet;
	}
	public void setTotalBet(double totalBet) {
		this.totalBet = totalBet;
	}
	
	public double[] getCarBet() {
		return carBet;
	}
	public void setCarBet(double[] carBet) {
		this.carBet = carBet;
	}
	
	public int getBetLevel() {
		return betLevel;
	}
	public void setBetLevel(int betLevel) {
		this.betLevel = betLevel;
	}
	
	
	public double[] getPlayerBetInfo() {
		return playerBetInfo;
	}
	public void setPlayerBetInfo(double[] playerBetInfo) {
		this.playerBetInfo = playerBetInfo;
	}
	@Override
	public String toString() {
		return "SingleBetInfo [roleid=" + roleid + ", car=" + car + ", bet=" + bet + ", betLevel=" + betLevel
				+ ", totalBet=" + totalBet + ", playerBetInfo=" + Arrays.toString(playerBetInfo) + ", carBet="
				+ Arrays.toString(carBet) + "]";
	}
	
}
