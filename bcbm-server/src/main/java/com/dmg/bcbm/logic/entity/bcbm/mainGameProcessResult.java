package com.dmg.bcbm.logic.entity.bcbm;

import java.util.List;
import java.util.Map.Entry;

public class mainGameProcessResult {
	// 游戏记录
	private List<BmbcWinInfo> gameRecord;
	// 当局游戏结果
	private BmbcWinInfo bmbcWinInfo;
	// 玩家赢赏结果
	private List<Entry<String, Double>> rewardSort;
	// 获得最高奖励玩家信息
	private String highRewardName;
	private double highRewardGold;
	// 玩家的roleid
	private String roleId;
	// 玩家下注的金币
	private double bet;
	// 玩家当前的金币
	private double gold;
	// 本局玩家输赢金币
	private double winLoseGold;
	// 本局庄家输赢金币
	private double bankerWinLoseGold;
	// 庄家信息
	private Banker banker1;
	public List<BmbcWinInfo> getGameRecord() {
		return gameRecord;
	}
	public void setGameRecord(List<BmbcWinInfo> gameRecord) {
		this.gameRecord = gameRecord;
	}
	public BmbcWinInfo getBmbcWinInfo() {
		return bmbcWinInfo;
	}
	public void setBmbcWinInfo(BmbcWinInfo bmbcWinInfo) {
		this.bmbcWinInfo = bmbcWinInfo;
	}
	public List<Entry<String, Double>> getRewardSort() {
		return rewardSort;
	}
	public void setRewardSort(List<Entry<String, Double>> rewardSort) {
		this.rewardSort = rewardSort;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public double getGold() {
		return gold;
	}
	public void setGold(double gold) {
		this.gold = gold;
	}
	public String getHighRewardName() {
		return highRewardName;
	}
	public void setHighRewardName(String highRewardName) {
		this.highRewardName = highRewardName;
	}
	
	public double getHighRewardGold() {
		return highRewardGold;
	}
	public void setHighRewardGold(double highRewardGold) {
		this.highRewardGold = highRewardGold;
	}
	public Banker getBanker1() {
		return banker1;
	}
	public void setBanker1(Banker banker1) {
		this.banker1 = banker1;
	}
	
	public double getWinLoseGold() {
		return winLoseGold;
	}
	public void setWinLoseGold(double winLoseGold) {
		this.winLoseGold = winLoseGold;
	}
	
	public double getBet() {
		return bet;
	}
	public void setBet(double bet) {
		this.bet = bet;
	}
	
	public double getBankerWinLoseGold() {
		return bankerWinLoseGold;
	}
	public void setBankerWinLoseGold(double bankerWinLoseGold) {
		this.bankerWinLoseGold = bankerWinLoseGold;
	}
	@Override
	public String toString() {
		return "mainGameProcessResult [gameRecord=" + gameRecord + ", bmbcWinInfo=" + bmbcWinInfo + ", rewardSort="
				+ rewardSort + ", highRewardName=" + highRewardName + ", highRewardGold=" + highRewardGold + ", roleId="
				+ roleId + ", bet=" + bet + ", gold=" + gold + ", winLoseGold=" + winLoseGold + ", banker1=" + banker1
				+ "]";
	}
	
}
