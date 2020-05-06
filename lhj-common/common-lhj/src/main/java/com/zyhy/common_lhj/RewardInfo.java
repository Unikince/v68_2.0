package com.zyhy.common_lhj;

import com.zyhy.common_lhj.Icon;

public class RewardInfo {
	// 中奖线路总数
	private int num;
	// 中奖图标
	private Icon icon;
	// 中奖图标个数
	private int WinNum;
	// 是否包含指定图标
	private boolean isContains;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Icon getIcon() {
		return icon;
	}
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	public int getWinNum() {
		return WinNum;
	}
	public void setWinNum(int winNum) {
		WinNum = winNum;
	}

	public boolean isContains() {
		return isContains;
	}
	public void setContains(boolean isContains) {
		this.isContains = isContains;
	}
	
	@Override
	public String toString() {
		return "RewardInfo [num=" + num + ", icon=" + icon + ", WinNum=" + WinNum + ", isContains=" + isContains + "]";
	}
	
}
