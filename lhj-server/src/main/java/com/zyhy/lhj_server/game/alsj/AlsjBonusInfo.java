/**
 * 
 */
package com.zyhy.lhj_server.game.alsj;

import com.zyhy.common_lhj.BetInfo;

/**
 * @author ASUS
 *
 */
public class AlsjBonusInfo {

	//下注信息
	private BetInfo betInfo;
	//免费次数
	private int num;
	//是否是免费游戏获得
	private boolean isFree;
	//总奖金
	private double gold;
	
	public AlsjBonusInfo(BetInfo betinfo) {
		this.betInfo = betinfo;
	}
	
	public AlsjBonusInfo() {
		
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
	

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	@Override
	public String toString() {
		return "AlsjBonusInfo [betInfo=" + betInfo + ", num=" + num + ", isFree=" + isFree + ", gold=" + gold + "]";
	}

	
}
