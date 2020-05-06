package com.zyhy.lhj_server.game.tgpd.test;

import java.util.HashMap;
import java.util.Map;

public class TgpdResult {
	 // 总下注次数
	 private int betCount;
	 // 总投注金额
	 private double totalBet ;
	// 实际游戏次数
	 private int gameCount;
	// 实际游戏下注次数
	 private int gameCount1;
	 private int gameCount2;
	 private int gameCount3;
	 // 总中奖次数
	 private int totalCount1;
	 private int totalCount2;
	 private int totalCount3;
	// 总奖励
	 private double totalreward1;
	 private double totalreward2;
	 private double totalreward3;
	// 免费游戏触发次数
	private int freeCount1;
	private int freeCount2;
	private int freeCount3;
	// 免费游戏总次数
	private int totoalFreeCount1;
	private int totoalFreeCount2;
	private int totoalFreeCount3;
	 // 免费游戏奖励
	 private double freeGameReward1;
	 private double freeGameReward2;
	 private double freeGameReward3;
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
	public int getGameCount1() {
		return gameCount1;
	}
	public void setGameCount1(int gameCount1) {
		this.gameCount1 = gameCount1;
	}
	public int getGameCount2() {
		return gameCount2;
	}
	public void setGameCount2(int gameCount2) {
		this.gameCount2 = gameCount2;
	}
	public int getGameCount3() {
		return gameCount3;
	}
	public void setGameCount3(int gameCount3) {
		this.gameCount3 = gameCount3;
	}
	public int getTotalCount1() {
		return totalCount1;
	}
	public void setTotalCount1(int totalCount1) {
		this.totalCount1 = totalCount1;
	}
	public int getTotalCount2() {
		return totalCount2;
	}
	public void setTotalCount2(int totalCount2) {
		this.totalCount2 = totalCount2;
	}
	public int getTotalCount3() {
		return totalCount3;
	}
	public void setTotalCount3(int totalCount3) {
		this.totalCount3 = totalCount3;
	}
	public double getTotalreward1() {
		return totalreward1;
	}
	public void setTotalreward1(double totalreward1) {
		this.totalreward1 = totalreward1;
	}
	public double getTotalreward2() {
		return totalreward2;
	}
	public void setTotalreward2(double totalreward2) {
		this.totalreward2 = totalreward2;
	}
	public double getTotalreward3() {
		return totalreward3;
	}
	public void setTotalreward3(double totalreward3) {
		this.totalreward3 = totalreward3;
	}

	public int getFreeCount1() {
		return freeCount1;
	}
	public void setFreeCount1(int freeCount1) {
		this.freeCount1 = freeCount1;
	}
	public int getFreeCount2() {
		return freeCount2;
	}
	public void setFreeCount2(int freeCount2) {
		this.freeCount2 = freeCount2;
	}
	public int getFreeCount3() {
		return freeCount3;
	}
	public void setFreeCount3(int freeCount3) {
		this.freeCount3 = freeCount3;
	}
	public int getTotoalFreeCount1() {
		return totoalFreeCount1;
	}
	public void setTotoalFreeCount1(int totoalFreeCount1) {
		this.totoalFreeCount1 = totoalFreeCount1;
	}
	public int getTotoalFreeCount2() {
		return totoalFreeCount2;
	}

	public void setTotoalFreeCount2(int totoalFreeCount2) {
		this.totoalFreeCount2 = totoalFreeCount2;
	}
	public int getTotoalFreeCount3() {
		return totoalFreeCount3;
	}
	public void setTotoalFreeCount3(int totoalFreeCount3) {
		this.totoalFreeCount3 = totoalFreeCount3;
	}
	public double getFreeGameReward1() {
		return freeGameReward1;
	}
	public void setFreeGameReward1(double freeGameReward1) {
		this.freeGameReward1 = freeGameReward1;
	}
	public double getFreeGameReward2() {
		return freeGameReward2;
	}
	public void setFreeGameReward2(double freeGameReward2) {
		this.freeGameReward2 = freeGameReward2;
	}
	public double getFreeGameReward3() {
		return freeGameReward3;
	}
	public void setFreeGameReward3(double freeGameReward3) {
		this.freeGameReward3 = freeGameReward3;
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
	@Override
	public String toString() {
		return "TestResult [总下注次数 =" + betCount 
				+ ", 总投注金额 =" + totalBet 
				+ ", 总游戏次数 =" + gameCount 
				+ ", 正常游戏下注次数1 =" + gameCount1 
				+ ", 正常游戏中奖次数1 =" + totalCount1
				+ ", 正常游戏奖励1 =" + totalreward1
				+ ", 免费游戏触发次数1 =" + freeCount1 
				+ ", 免费游戏中奖次数1 =" + totoalFreeCount1 
				+ ", 免费游戏奖励1 =" + freeGameReward1 
				+ ", 正常游戏下注次数2 =" + gameCount2 
				+ ", 正常游戏中奖次数2 =" + totalCount2
				+ ", 正常游戏奖励2 =" + totalreward2 
				+ ", 免费游戏触发次数2 =" + freeCount2 
				+ ", 免费游戏中奖次数2 =" + totoalFreeCount2 
				+ ", 免费游戏奖励2 =" + freeGameReward2 
				+ ", 正常游戏下注次数3 =" + gameCount3 
				+ ", 正常游戏中奖次数3 =" + totalCount3
				+ ", 正常游戏奖励3 =" + totalreward3 
				+ ", 免费游戏触发次数3 =" + freeCount3 
				+ ", 免费游戏中奖次数3 =" + totoalFreeCount3 
				+ ", 免费游戏奖励3 =" + freeGameReward3 
				+ ", 中奖概率 =" + totalRewardlv 
				+ ", 收益率 ="+ totalIncomelv + "]";
	}
}
