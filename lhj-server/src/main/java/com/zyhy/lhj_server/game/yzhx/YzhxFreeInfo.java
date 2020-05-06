package com.zyhy.lhj_server.game.yzhx;

import com.zyhy.common_lhj.BetInfo;


public class YzhxFreeInfo {

	//下注信息
	private BetInfo betInfo;
	// 下注档位
	private int betLine;
	//免费次数
	private int freeNum;
	//总奖金
	private double gold;
	
	public YzhxFreeInfo(){}
	
	public YzhxFreeInfo(BetInfo betInfo){
		this.betInfo = betInfo;
	}

	public BetInfo getBetInfo() {
		return betInfo;
	}

	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}

	public int getFreeNum() {
		return freeNum;
	}

	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}


	public int getBetLine() {
		return betLine;
	}

	public void setBetLine(int betLine) {
		this.betLine = betLine;
	}

	@Override
	public String toString() {
		return "YzhxFreeInfo [betInfo=" + betInfo + ", betLine=" + betLine + ", freeNum=" + freeNum + ", gold=" + gold
				+ "]";
	}



	
}
