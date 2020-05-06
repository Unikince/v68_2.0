package com.dmg.bcbm.logic.entity.bcbm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBetInfo {
	// 玩家id
	private String roleid;
	// 下注总额度
	private double totalBet;
	// 下注详情(K:下注车辆 Map K: 下注种类 V: 下注次数)
	private Map<Integer, List<Double>> betinfo;
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public double getTotalBet() {
		return totalBet;
	}
	public void setTotalBet(double totalBet) {
		this.totalBet = totalBet;
	}
	
	public Map<Integer, List<Double>> getBetinfo() {
		return betinfo;
	}
	public void setBetinfo(Map<Integer, List<Double>> betinfo) {
		this.betinfo = betinfo;
	}
	@Override
	public String toString() {
		return "BetInfo [roleid=" + roleid + ", totalBet=" + totalBet + ", betinfo=" + betinfo + "]";
	}

}
