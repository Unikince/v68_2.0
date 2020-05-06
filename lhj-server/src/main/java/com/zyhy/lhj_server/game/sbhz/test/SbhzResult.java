package com.zyhy.lhj_server.game.sbhz.test;

import java.util.HashMap;
import java.util.Map;

public class SbhzResult {
	 // 总下注次数
	 private int betCount;
	// 实际游戏次数
	 private int gameCount = 0;
	 // 总投注金额
	 private double totalBet ;
	 // 总中奖次数
	 private int totalCount ;
	 // 总奖励
	 private double totalreward;
	 // 总中奖概率
	 private double totalRewardlv;
	 // 总收益率
	 private double totalIncomelv;
	 // 中奖线路以及次数
	 private Map<String, String> iconInfo = new HashMap<>();
	// wild_1次数
	private int wild_1 = 0;
	// wild_2次数
	private int wild_2 = 0;
	// wild_3次数
	private int wild_3 = 0;
	// 免费wild_3次数
	private int freewild_3 = 0;
	// 免费wild_3奖励
	private double freewild_3Reward = 0;
	// wild1奖励
	private double wild_1Reward = 0;
	// wild2奖励
	private double wild_2Reward = 0;
	// wild3奖励
	private double wild_3Reward = 0;
	

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


	public double getFreewild_3Reward() {
		return freewild_3Reward;
	}

	public void setFreewild_3Reward(double freewild_3Reward) {
		this.freewild_3Reward = freewild_3Reward;
	}

	public double getWild_3Reward() {
		return wild_3Reward;
	}

	public void setWild_3Reward(double wild_3Reward) {
		this.wild_3Reward = wild_3Reward;
	}

	public double getWild_1Reward() {
		return wild_1Reward;
	}

	public void setWild_1Reward(double wild_1Reward) {
		this.wild_1Reward = wild_1Reward;
	}

	public double getWild_2Reward() {
		return wild_2Reward;
	}

	public void setWild_2Reward(double wild_2Reward) {
		this.wild_2Reward = wild_2Reward;
	}

	public int getWild_1() {
		return wild_1;
	}

	public void setWild_1(int wild_1) {
		this.wild_1 = wild_1;
	}

	public int getWild_2() {
		return wild_2;
	}

	public void setWild_2(int wild_2) {
		this.wild_2 = wild_2;
	}

	public int getWild_3() {
		return wild_3;
	}

	public void setWild_3(int wild_3) {
		this.wild_3 = wild_3;
	}

	public int getFreewild_3() {
		return freewild_3;
	}

	public void setFreewild_3(int freewild_3) {
		this.freewild_3 = freewild_3;
	}

	@Override
	public String toString() {
		return "TestResult [总下注次数 =" + betCount 
				+ ", 总游戏次数 =" + gameCount 
				+ ", 总投注金额 =" + totalBet 
				+ ", 正常游戏中奖次数 =" + totalCount
				+ ", 正常游戏奖励 =" + totalreward 
				+ ", wild_1次数 =" + wild_1
				+ ", wild_2次数 =" + wild_2
				+ ", wild_3次数 =" + wild_3
				+ ", 重转freewild_3次数 =" + freewild_3
				+ ", 重转freewild_3奖励 =" + freewild_3Reward 
				+ ", wild_1奖励 =" + wild_1Reward 
				+ ", wild_2奖励 =" + wild_2Reward 
				+ ", wild_3奖励 =" + wild_3Reward 
				+ ", 中奖概率 =" + totalRewardlv 
				+ ", 收益率 ="+ totalIncomelv + "]";
	}
}
