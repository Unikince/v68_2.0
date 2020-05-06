package com.zyhy.lhj_server.game.lll.test;

import java.util.HashMap;
import java.util.Map;

public class LllResult {
	 // 总下注次数
	 private int betCount;
	// 实际游戏次数
	 private int gameCount = 0;
	 // 总投注金额
	 private double totalBet ;
	 // 总中奖次数
	 private int totalCount ;
	// 免费游戏触发次数
	private int freeCount;
	// 免费游戏总次数
	private int totoalFreeCount;
	 // 总奖励
	 private double totalreward;
	// T1出现次数
	private int t1 = 0;
	// T1总奖励
	private double t1reward = 0;
	// T2出现次数
	private int t2 = 0;
	// T2总奖励
	private double t2reward = 0;
	// T3出现次数
	private int t3 = 0;
	// T3总奖励
	private double t3reward = 0;
	// 任意3个次数
	private int any3 = 0;
	// 任意3个次数奖励
	private double any3reward = 0;
	 // 免费游戏奖励
	 private double freeGameReward;
	 // 总中奖概率
	 private double totalRewardlv;
	 // 总收益率
	 private double totalIncomelv;
	 // 中奖线路以及次数
	 private Map<String, String> iconInfo = new HashMap<>();
	

	public int getBetCount() {
		return betCount;
	}

	public void setBetCount(int betCount) {
		this.betCount = betCount;
	}

	public double getTotalBet() {
		return totalBet;
	}

	public void setTotalBet(double totalBet) {
		this.totalBet = totalBet;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public double getTotalreward() {
		return totalreward;
	}

	public void setTotalreward(double totalreward) {
		this.totalreward = totalreward;
	}

	public double getTotalRewardlv() {
		return totalRewardlv;
	}

	public void setTotalRewardlv(double totalRewardlv) {
		this.totalRewardlv = totalRewardlv;
	}

	public double getTotalIncomelv() {
		return totalIncomelv;
	}

	public void setTotalIncomelv(double totalIncomelv) {
		this.totalIncomelv = totalIncomelv;
	}
	
	public Map<String, String> getIconInfo() {
		return iconInfo;
	}

	public void setIconInfo(Map<String, String> iconInfo) {
		this.iconInfo = iconInfo;
	}

	public int getGameCount() {
		return gameCount;
	}

	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}

	public double getFreeGameReward() {
		return freeGameReward;
	}

	public void setFreeGameReward(double freeGameReward) {
		this.freeGameReward = freeGameReward;
	}

	public int getFreeCount() {
		return freeCount;
	}

	public void setFreeCount(int freeCount) {
		this.freeCount = freeCount;
	}

	public int getTotoalFreeCount() {
		return totoalFreeCount;
	}

	public void setTotoalFreeCount(int totoalFreeCount) {
		this.totoalFreeCount = totoalFreeCount;
	}

	public int getT1() {
		return t1;
	}

	public void setT1(int t1) {
		this.t1 = t1;
	}

	public double getT1reward() {
		return t1reward;
	}

	public void setT1reward(double t1reward) {
		this.t1reward = t1reward;
	}

	public int getT2() {
		return t2;
	}

	public void setT2(int t2) {
		this.t2 = t2;
	}

	public double getT2reward() {
		return t2reward;
	}

	public void setT2reward(double t2reward) {
		this.t2reward = t2reward;
	}

	public int getT3() {
		return t3;
	}

	public void setT3(int t3) {
		this.t3 = t3;
	}

	public double getT3reward() {
		return t3reward;
	}

	public void setT3reward(double t3reward) {
		this.t3reward = t3reward;
	}

	public int getAny3() {
		return any3;
	}

	public void setAny3(int any3) {
		this.any3 = any3;
	}

	public double getAny3reward() {
		return any3reward;
	}

	public void setAny3reward(double any3reward) {
		this.any3reward = any3reward;
	}

	@Override
	public String toString() {
		return "TestResult [总下注次数 =" + betCount 
				+ ", 正常游戏次数 =" + gameCount 
				+ ", 总投注金额 =" + totalBet 
				+ ", 正常游戏中奖次数 =" + totalCount
				+ ", 正常游戏奖励 =" + totalreward 
				+ ", T1中奖次数 =" + t1 
				+ ", T1中奖奖励 =" + t1reward 
				+ ", T2中奖次数 =" + t2 
				+ ", T2中奖奖励 =" + t2reward 
				+ ", T3中奖次数 =" + t3 
				+ ", T3中奖奖励 =" + t3reward 
				+ ", 任意3个中奖次数 =" + any3 
				+ ", 任意3个中奖奖励 =" + any3reward 
				+ ", 中奖概率 =" + totalRewardlv 
				+ ", 收益率 ="+ totalIncomelv + "]";
	}
}
