package com.zyhy.lhj_server.game.lqjx.test;

import java.util.HashMap;
import java.util.Map;

public class lqjxResult {
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
	//红利游戏触发次数
	private int bonusCount = 0;
	// 免费游戏奖励
	private double bonusReward = 0;
	 // 总中奖概率
	 private double totalRewardlv;
	 // 总收益率
	 private double totalIncomelv;
	 // 中奖线路以及次数
	 private Map<String, String> iconInfo = new HashMap<>();
	// 正常游戏掉落次数
	private  int dorpgameCount;
	// 正常游戏diaol中奖次数
	private  int dorptotalCount;
	// 正常游戏掉落奖励
	private double dorptotalReward;
	// 免费游戏掉落触发次数
	private  int dorpfreeCount;
	// 免费游戏掉落总次数
	private  int dorptotoalFreeCount;
	// 免费游戏掉落奖励
	private  double dorpfreeGameReward;
	// 免费游戏奖励次数
	private double freeRewardCount;
	
	private double totalRewardwild;
	private  int totalCountwild;
	private double ohertotalRewardwild;
	public double getFreeRewardCount() {
		return freeRewardCount;
	}

	public void setFreeRewardCount(double freeRewardCount) {
		this.freeRewardCount = freeRewardCount;
	}

	public double getTotalRewardwild() {
		return totalRewardwild;
	}

	public double getOhertotalRewardwild() {
		return ohertotalRewardwild;
	}

	public void setOhertotalRewardwild(double ohertotalRewardwild) {
		this.ohertotalRewardwild = ohertotalRewardwild;
	}

	public void setTotalRewardwild(double totalRewardwild) {
		this.totalRewardwild = totalRewardwild;
	}

	public int getTotalCountwild() {
		return totalCountwild;
	}

	public void setTotalCountwild(int totalCountwild) {
		this.totalCountwild = totalCountwild;
	}

	public int getDorpgameCount() {
		return dorpgameCount;
	}

	public void setDorpgameCount(int dorpgameCount) {
		this.dorpgameCount = dorpgameCount;
	}

	public int getDorptotalCount() {
		return dorptotalCount;
	}

	public void setDorptotalCount(int dorptotalCount) {
		this.dorptotalCount = dorptotalCount;
	}

	public double getDorptotalReward() {
		return dorptotalReward;
	}

	public void setDorptotalReward(double dorptotalReward) {
		this.dorptotalReward = dorptotalReward;
	}

	public int getDorpfreeCount() {
		return dorpfreeCount;
	}

	public void setDorpfreeCount(int dorpfreeCount) {
		this.dorpfreeCount = dorpfreeCount;
	}

	public int getDorptotoalFreeCount() {
		return dorptotoalFreeCount;
	}

	public void setDorptotoalFreeCount(int dorptotoalFreeCount) {
		this.dorptotoalFreeCount = dorptotoalFreeCount;
	}

	public double getDorpfreeGameReward() {
		return dorpfreeGameReward;
	}

	public void setDorpfreeGameReward(double dorpfreeGameReward) {
		this.dorpfreeGameReward = dorpfreeGameReward;
	}

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


	public int getBonusCount() {
		return bonusCount;
	}

	public void setBonusCount(int bonusCount) {
		this.bonusCount = bonusCount;
	}

	public double getBonusReward() {
		return bonusReward;
	}

	public void setBonusReward(double bonusReward) {
		this.bonusReward = bonusReward;
	}

	@Override
	public String toString() {
		return "TestResult [总下注次数 =" + betCount 
				+ ", 总投注金额 =" + totalBet 
				+ ", 正常游戏次数 =" + gameCount 
				+ ", 正常游戏中奖次数 =" + totalCount
				+ ", 正常游戏wild中奖次数 =" + totalCountwild
				+ ", 正常游戏奖励 =" + totalreward 
				+ ", 正常游戏wildx3奖励 =" + totalRewardwild
				+ ", 正常游戏其他wild奖励 =" + ohertotalRewardwild
				+ ", 正常游戏掉落次数 =" + dorpgameCount 
				+ ", 正常游戏掉落中奖次数 =" + dorptotalCount
				+ ", 正常游戏掉落奖励 =" + dorptotalReward 
				+ ", 免费游戏触发次数 =" + freeCount 
				+ ", 免费游戏总次数 =" + totoalFreeCount 
				+ ", 免费游戏中奖次数 =" + freeRewardCount 
				+ ", 免费游戏奖励 =" + freeGameReward 
				+ ", 免费游戏掉落次数 =" + dorpfreeCount 
				+ ", 免费游戏掉落中奖次数 =" + dorptotoalFreeCount 
				+ ", 免费游戏掉落奖励 =" + dorpfreeGameReward 
				+ ", 中奖概率 =" + totalRewardlv 
				+ ", 收益率 ="+ totalIncomelv + "]";
	}
}
