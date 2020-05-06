package com.zyhy.lhj_server.game.ajxz;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.BetInfo;

/**
 * @author DPC
 * @version 创建时间：2019年2月27日 上午10:18:13
 */
public class AjxzReplenish {
	//下注信息
	private BetInfo betInfo;
	//是否补充数据
	private boolean isReplenish;
	//是否是免费游戏
	private boolean isFree;
	//掉落的次数
	private int number;
	//全是wild所在的列
	private List<Integer> wild = new ArrayList<Integer>();
	
	public AjxzReplenish(BetInfo betInfo){
		this.betInfo = betInfo;	
	}
	public AjxzReplenish(){
		
	}
	
	public boolean isReplenish() {
		return isReplenish;
	}
	public void setReplenish(boolean isReplenish) {
		this.isReplenish = isReplenish;
	}
	public BetInfo getBetInfo() {
		return betInfo;
	}
	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}

	public boolean isFree() {
		return isFree;
	}

	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	public List<Integer> getWild() {
		return wild;
	}
	public void setWild(List<Integer> wild) {
		this.wild = wild;
	}	
}
