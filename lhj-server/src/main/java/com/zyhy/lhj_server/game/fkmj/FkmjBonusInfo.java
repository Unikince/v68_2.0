package com.zyhy.lhj_server.game.fkmj;

import com.zyhy.common_lhj.BetInfo;


public class FkmjBonusInfo {

	//下注信息
	private BetInfo betInfo;
	// 下注档位
	private int betLine;
	//免费次数
	private int num;
	//连续中奖次数
	private int count;
	//免费游戏总奖金
	private double gold;
	// 中免费时主游戏奖励
	private double reward;
	
	public FkmjBonusInfo(){}
	
	public FkmjBonusInfo(BetInfo betInfo){
		this.betInfo = betInfo;
	}

	public BetInfo getBetInfo() {
		return betInfo;
	}

	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getBetLine() {
		return betLine;
	}

	public void setBetLine(int betLine) {
		this.betLine = betLine;
	}

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	@Override
	public String toString() {
		return "FkmjBonusInfo [betInfo=" + betInfo + ", betLine=" + betLine + ", num=" + num + ", count=" + count
				+ ", gold=" + gold + ", reward=" + reward + "]";
	}

}
