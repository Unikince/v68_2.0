package com.zyhy.lhj_server.game.ajxz;

import com.zyhy.common_lhj.BetInfo;


public class AjxzFreeInfo {

	//下注信息
	private BetInfo betInfo;
	// 下注档位
	private int betLine;
	//免费5次
	private int freeNum_5;
	//免费10次
	private int freeNum_10;
	//免费15次
	private int freeNum_15;
	// 玩家选择免费类型
	private int type;

	//总奖金
	private double gold;
	
	public AjxzFreeInfo(){}
	
	public AjxzFreeInfo(BetInfo betInfo){
		this.betInfo = betInfo;
	}

	public BetInfo getBetInfo() {
		return betInfo;
	}

	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}

	public int getFreeNum_5() {
		return freeNum_5;
	}

	public void setFreeNum_5(int freeNum_5) {
		this.freeNum_5 = freeNum_5;
	}

	public int getFreeNum_10() {
		return freeNum_10;
	}

	public void setFreeNum_10(int freeNum_10) {
		this.freeNum_10 = freeNum_10;
	}

	public int getFreeNum_15() {
		return freeNum_15;
	}

	public void setFreeNum_15(int freeNum_15) {
		this.freeNum_15 = freeNum_15;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
		return "AjxzFreeInfo [betInfo=" + betInfo + ", betLine=" + betLine + ", freeNum_5=" + freeNum_5
				+ ", freeNum_10=" + freeNum_10 + ", freeNum_15=" + freeNum_15 + ", type=" + type + ", gold=" + gold
				+ "]";
	}




}
