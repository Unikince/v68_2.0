package com.dmg.zhajinhuaserver.model.bean;

public class Robot extends Player{

	/****************机器人特性********************/

	// 闷牌概率
	private int menPoker;
	// 比牌概率
	private int battlePoker;
	// 加注概率
	private int addbetGold;
	// 大牌装小牌概率
	private int zhuangbigPoker;
	// 小牌装大牌概率
	private int zhuangsmallPoker;
	// 正常玩概率
	private int commongamePoker;
	// 偷看手牌概率
	private int peepPoker;

	public String robotLvToString() {
		return "Robot [menPoker=" + menPoker + ", battlePoker=" + battlePoker
				+ ", addbetGold=" + addbetGold + ", zhuangbigPoker="
				+ zhuangbigPoker + ", zhuangsmallPoker=" + zhuangsmallPoker
				+ ", commongamePoker=" + commongamePoker + ", peepPoker="
				+ peepPoker + "]";
	}
	public int getMenPoker() {
		return menPoker;
	}
	public void setMenPoker(int menPoker) {
		if (menPoker > 0) {
			this.menPoker = menPoker;
		}else {
			this.menPoker = 0;
		}
	}
	public void updateRobotAndPrivateField(Double newgold) {
		this.gold = newgold < 0.0D ? 0.0D : newgold;
	}
	public int getZhuangbigPoker() {
		return zhuangbigPoker;
	}
	public void setZhuangbigPoker(int zhuangbigPoker) {
		this.zhuangbigPoker = zhuangbigPoker;
	}
	public int getZhuangsmallPoker() {
		return zhuangsmallPoker;
	}
	public void setZhuangsmallPoker(int zhuangsmallPoker) {
		this.zhuangsmallPoker = zhuangsmallPoker;
	}
	public int getPeepPoker() {
		return peepPoker;
	}
	public void setPeepPoker(int peepPoker) {
		this.peepPoker = peepPoker;
	}
	public int getBattlePoker() {
		return battlePoker;
	}
	public void setBattlePoker(int battlePoker) {
		this.battlePoker = battlePoker;
	}
	public int getCommongamePoker() {
		return commongamePoker;
	}
	public void setCommongamePoker(int commongamePoker) {
		this.commongamePoker = commongamePoker;
	}
	public int getAddbetGold() {
		return addbetGold;
	}
	public void setAddbetGold(int addbetGold) {
		this.addbetGold = addbetGold;
	}
}
