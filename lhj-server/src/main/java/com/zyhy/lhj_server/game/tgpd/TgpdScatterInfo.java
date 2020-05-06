package com.zyhy.lhj_server.game.tgpd;

import com.zyhy.common_lhj.BetInfo;

/**
 * @author ASUS
 *
 */
public class TgpdScatterInfo {

	//下注信息
	private BetInfo betInfo;
	// 下注档位
	private int betLine;
	// 回合id
	private String roundId;
	//免费次数
	private int num;
	// 免费游戏奖励倍数
	private int mul;
	//单次奖金
	private double singleReward;
	//总奖金
	private double gold;
	
	public TgpdScatterInfo(){}
	
	public TgpdScatterInfo(BetInfo betInfo){
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
	public int getMul() {
		return mul;
	}
	public void setMul(int mul) {
		this.mul = mul;
	}
	
	public int getBetLine() {
		return betLine;
	}

	public void setBetLine(int betLine) {
		this.betLine = betLine;
	}

	public String getRoundId() {
		return roundId;
	}

	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}

	@Override
	public String toString() {
		return "TgpdScatterInfo [betInfo=" + betInfo + ", betLine=" + betLine + ", roundId=" + roundId + ", num=" + num
				+ ", mul=" + mul + ", singleReward=" + singleReward + ", gold=" + gold + "]";
	}


}
