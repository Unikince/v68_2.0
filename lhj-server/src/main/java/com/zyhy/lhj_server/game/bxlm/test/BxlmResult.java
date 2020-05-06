package com.zyhy.lhj_server.game.bxlm.test;

import java.util.HashMap;
import java.util.Map;

public class BxlmResult {
	 // 总下注次数
	 private int betCount;
	// 实际游戏次数
	 private int gameCount;
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
	 private double freetotalCount ;
	// 免费游戏奖励
	 private double  freetotalReward;
	 private  int wild1count;
	 private  int wild2count;
	 private  int wild3count;
	 private  int wild4count;
	 private  int wild5count;
	 private double wild1total;
	 private double wild2total;
	 private double wild3total;
	 private double wild4total;
	 private double wild5total;
	 private double wildtotalReward;

	public int getBetCount() {
		return betCount;
	}

	public int getWild1count() {
		return wild1count;
	}

	public double getWild1total() {
		return wild1total;
	}

	public void setWild1total(double wild1total) {
		this.wild1total = wild1total;
	}

	public double getWild2total() {
		return wild2total;
	}

	public void setWild2total(double wild2total) {
		this.wild2total = wild2total;
	}

	public double getWild3total() {
		return wild3total;
	}

	public void setWild3total(double wild3total) {
		this.wild3total = wild3total;
	}

	public double getWild4total() {
		return wild4total;
	}

	public void setWild4total(double wild4total) {
		this.wild4total = wild4total;
	}

	public double getWild5total() {
		return wild5total;
	}

	public void setWild5total(double wild5total) {
		this.wild5total = wild5total;
	}

	public void setWild1count(int wild1count) {
		this.wild1count = wild1count;
	}

	public int getWild2count() {
		return wild2count;
	}

	public void setWild2count(int wild2count) {
		this.wild2count = wild2count;
	}

	public int getWild3count() {
		return wild3count;
	}

	public void setWild3count(int wild3count) {
		this.wild3count = wild3count;
	}

	public int getWild4count() {
		return wild4count;
	}

	public void setWild4count(int wild4count) {
		this.wild4count = wild4count;
	}

	public int getWild5count() {
		return wild5count;
	}

	public void setWild5count(int wild5count) {
		this.wild5count = wild5count;
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

	public double getFreetotalCount() {
		return freetotalCount;
	}

	public void setFreetotalCount(double freetotalCount) {
		this.freetotalCount = freetotalCount;
	}

	public double getFreetotalReward() {
		return freetotalReward;
	}

	public void setFreetotalReward(double freetotalReward) {
		this.freetotalReward = freetotalReward;
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

	public double getWildtotalReward() {
		return wildtotalReward;
	}

	public void setWildtotalReward(double wildtotalReward) {
		this.wildtotalReward = wildtotalReward;
	}

	@Override
	public String toString() {
		return "TestResult [总下注次数 =" + betCount 
				+ ", 正常游戏次数 =" + gameCount 
				+ ", 总投注金额 =" + totalBet 
				+ ", 正常游戏中奖次数 =" + totalCount
				+ ", 正常游戏奖励 =" + totalreward 
				+ ", 正常游戏加倍奖励 =" + wildtotalReward 
				+ ", wild x 1出现次数 =" + wild1count 
				+ ", wild x 2出现次数 =" + wild2count 
				+ ", wild x 3出现次数 =" + wild3count 
				+ ", wild x 4出现次数 =" + wild4count 
				+ ", wild x 5出现次数 =" + wild5count 
				+ ", wild1总奖励 =" + wild1total 
				+ ", wild2总奖励 =" + wild2total 
				+ ", wild3总奖励 =" + wild3total 
				+ ", wild4总奖励 =" + wild4total 
				+ ", wild5总奖励 =" + wild5total 
				+ ", 免费游戏触发次数 =" + freeCount 
				+ ", 免费游戏中奖次数 =" + freetotalCount 
				+ ", 免费游戏奖励 =" + freetotalReward 
				+ ", 中奖概率 =" + totalRewardlv 
				+ ", 收益率 ="+ totalIncomelv + "]";
	}
}
