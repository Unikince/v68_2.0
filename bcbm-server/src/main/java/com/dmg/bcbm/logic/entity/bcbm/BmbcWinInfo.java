package com.dmg.bcbm.logic.entity.bcbm;

public class BmbcWinInfo {
	// 中奖车辆
	private int car;
	// 名称
	private String name;
	// 赔率
	private int lv;
	// 中奖位置
	private int local;
	
	public int getCar() {
		return car;
	}
	public void setCar(int car) {
		this.car = car;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLv() {
		return lv;
	}
	public void setLv(int lv) {
		this.lv = lv;
	}
	public int getLocal() {
		return local;
	}
	public void setLocal(int local) {
		this.local = local;
	}
	
	@Override
	public String toString() {
		return "BmbcWinInfo [car=" + car + ", name=" + name + ", lv=" + lv + ", local=" + local + "]";
	}

}
