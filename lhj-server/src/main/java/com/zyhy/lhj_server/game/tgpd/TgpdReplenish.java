package com.zyhy.lhj_server.game.tgpd;

import com.zyhy.common_lhj.BetInfo;

/**
 * @author DPC
 * @version 创建时间：2019年2月27日 上午10:18:13
 */
public class TgpdReplenish {
	//下注信息
	private BetInfo betInfo;
	// 是否掉落
	private boolean isDrop;
	// 掉落奖励
	private double dropReward;
	// 掉落窗口信息
	private TgpdDorpInfo tgpdDorpInfo;
	public TgpdReplenish(BetInfo betInfo){
		this.betInfo = betInfo;	
	}
	public TgpdReplenish(){
	}
	public BetInfo getBetInfo() {
		return betInfo;
	}
	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}
	public boolean isDrop() {
		return isDrop;
	}
	public void setDrop(boolean isDrop) {
		this.isDrop = isDrop;
	}
	public double getDropReward() {
		return dropReward;
	}
	public void setDropReward(double dropReward) {
		this.dropReward = dropReward;
	}
	
	public TgpdDorpInfo getTgpdDorpInfo() {
		return tgpdDorpInfo;
	}
	public void setTgpdDorpInfo(TgpdDorpInfo tgpdDorpInfo) {
		this.tgpdDorpInfo = tgpdDorpInfo;
	}
	@Override
	public String toString() {
		return "TgpdReplenish [betInfo=" + betInfo + ", isDrop=" + isDrop + ", dropReward=" + dropReward
				+ ", tgpdDorpInfo=" + tgpdDorpInfo + "]";
	}
	
	
}
