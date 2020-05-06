package com.zyhy.lhj_server.game.sbhz;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_lhj.Window;

public class SbhzWildMonKey {
	//下注信息
	private BetInfo betInfo;
	//是否要进行重转
	private boolean isMonKey;
	// 窗口信息
	private List<Window> ws = new ArrayList<Window>();
	//需要重转的列
	private List<Integer> wild;
	public boolean isMonKey() {
		return isMonKey;
	}
	public void setMonKey(boolean isMonKey) {
		this.isMonKey = isMonKey;
	}
	public List<Window> getWs() {
		return ws;
	}
	public void setWs(List<Window> ws) {
		this.ws = ws;
	}
	public List<Integer> getWild() {
		return wild;
	}
	public void setWild(List<Integer> wild) {
		this.wild = wild;
	}
	public BetInfo getBetInfo() {
		return betInfo;
	}
	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}
	@Override
	public String toString() {
		return "SbhzWildMonKey [betInfo=" + betInfo + ", isMonKey=" + isMonKey + ", ws=" + ws + ", wild=" + wild + "]";
	}
	
	
}
