/**
 * 
 */
package com.zyhy.lhj_server.game.gghz;

import com.zyhy.common_lhj.Bet;

/**
 * @author linanjun
 * 下注限制信息
 */
public class GghzBetLimitInfo {
	
	private int id;
	private double mincoin;
	private double maxcoin;
	private double betcoin;
	
	public GghzBetLimitInfo(Bet bet) {
		this.id = bet.getId();
		this.mincoin = bet.getMin();
		this.maxcoin = bet.getMax();
		this.betcoin = bet.getBetcoin();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getMincoin() {
		return mincoin;
	}

	public void setMincoin(double mincoin) {
		this.mincoin = mincoin;
	}

	public double getMaxcoin() {
		return maxcoin;
	}

	public void setMaxcoin(double maxcoin) {
		this.maxcoin = maxcoin;
	}

	public double getBetcoin() {
		return betcoin;
	}

	public void setBetcoin(double betcoin) {
		this.betcoin = betcoin;
	}
	
}
