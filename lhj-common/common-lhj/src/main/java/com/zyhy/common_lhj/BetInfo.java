/**
 * 
 */
package com.zyhy.common_lhj;

import com.zyhy.common_server.util.NumberTool;

/**
 * @author ASUS
 *
 */
public class BetInfo {
	
	public final static int TYPE_NORMAL = 1;
	
	public final static int TYPE_FREE = 2;
	// 线注
	private double gold;
	// 硬币数
	private int num;
	// 总投注
	private double totalBet;
	/**
	 * 1普通 2免费
	 */
	private int type = 1;
	
	public BetInfo(){}

	public BetInfo(double bet, int betnum, double totalBet) {
		this.gold = bet;
		this.num = betnum;
		this.totalBet = totalBet;
	}

	public BetInfo(double bet, int betnum){
		this.gold = bet;
		this.num = betnum;
	}
	

	public double getGold() {
		return gold;
	}

	public void setGold(double gold) {
		this.gold = gold;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getTotalBet() {
		return totalBet;
	}

	public void setTotalBet(double totalBet) {
		this.totalBet = totalBet;
	}

	public double total(){
		return NumberTool.multiply(this.gold, this.num).doubleValue();
	}
	
	@Override
	public String toString() {
		return "BetInfo [gold=" + gold + ", num=" + num + ", type=" + type
				+ ", totalBet=" + totalBet + "]";
	}
	
}
