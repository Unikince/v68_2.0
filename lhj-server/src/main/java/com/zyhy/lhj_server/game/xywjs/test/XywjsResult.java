package com.zyhy.lhj_server.game.xywjs.test;

import java.util.HashMap;
import java.util.Map;

public class XywjsResult {
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

	@Override
	public String toString() {
		return "TestResult [总下注次数 =" + betCount 
				+ ", 总投注金额 =" + totalBet 
				+ ", 正常游戏次数 =" + gameCount 
				+ ", 正常游戏中奖次数 =" + totalCount
				+ ", 正常游戏的总奖励 =" + totalreward 
				+ ", lucky游戏总次数 =" + totoalFreeCount 
				+ ", lucky游戏中奖次数 =" + freeCount 
				+ ", lucky游戏总奖励 =" + freeGameReward 
				+ ", 中奖概率 =" + totalRewardlv 
				+ ", 收益率 ="+ totalIncomelv + "]";
	}
}
