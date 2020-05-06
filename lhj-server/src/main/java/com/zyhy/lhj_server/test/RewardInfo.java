package com.zyhy.lhj_server.test;

import java.util.List;


public class RewardInfo {
	// 中奖线路总数
	private int num;
	// 中奖图标list
	private List<WindowInfo> winIcon;
	// 中奖图标
	private String icon;
	// 中奖图标个数
	private int WinNum;
	// 中奖金额
	private double reward;
	// 是否包含指定图标
	private boolean isContains;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public List<WindowInfo> getWinIcon() {
		return winIcon;
	}
	public void setWinIcon(List<WindowInfo> winIcon) {
		this.winIcon = winIcon;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getWinNum() {
		return WinNum;
	}
	public void setWinNum(int winNum) {
		WinNum = winNum;
	}
	public double getReward() {
		return reward;
	}
	public void setReward(double reward) {
		this.reward = reward;
	}
	public boolean isContains() {
		return isContains;
	}
	public void setContains(boolean isContains) {
		this.isContains = isContains;
	}
	@Override
	public String toString() {
		return "RewardInfo [num=" + num + ", winIcon=" + winIcon + ", icon=" + icon + ", WinNum=" + WinNum + ", reward="
				+ reward + ", isContains=" + isContains + "]";
	}
	
	
}
