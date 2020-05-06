package com.zyhy.lhj_server.game.bqtp;

import com.zyhy.common_lhj.BetInfo;

/**
 * @author ASUS
 *
 */
public class BqtpScatterInfo {

	//下注信息
	private BetInfo betInfo;
	//免费次数
	private int num;
	//连续中奖次数
	private int count;
	// 下注档位
	private int betLine;
	//单次奖金
	private double singleReward;
	//总奖金
	private double gold;
	
	public BqtpScatterInfo(){}
	
	public BqtpScatterInfo(BetInfo betInfo){
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

	public double getSingleReward() {
		return singleReward;
	}

	public void setSingleReward(double singleReward) {
		this.singleReward = singleReward;
	}

	public int getBetLine() {
		return betLine;
	}

	public void setBetLine(int betLine) {
		this.betLine = betLine;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "BqtpScatterInfo [betInfo=" + betInfo + ", num=" + num + ", count=" + count + ", betLine=" + betLine
				+ ", singleReward=" + singleReward + ", gold=" + gold + "]";
	}



}
