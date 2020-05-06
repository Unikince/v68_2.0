/**
 * 
 */
package com.zyhy.lhj_server.game.gsgl;

import com.zyhy.common_lhj.BetInfo;

/**
 * @author ASUS
 *
 */
public class GsglBonusInfo {

	//下注信息
	private BetInfo betInfo;
	// 下注档位
	private int betLine;
	//免费次数
	private int num;
	//倍数
	private int mul;
	//总奖金
	private double gold;
	
	public GsglBonusInfo(){}
	
	public GsglBonusInfo(BetInfo betInfo){
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

	public int getMul() {
		return mul;
	}

	public void setMul(int mul) {
		this.mul = mul;
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
		return "GsglBonusInfo [betInfo=" + betInfo + ", betLine=" + betLine + ", num=" + num + ", mul=" + mul
				+ ", gold=" + gold + "]";
	}

	
}
