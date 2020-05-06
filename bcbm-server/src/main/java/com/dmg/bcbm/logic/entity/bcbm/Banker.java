package com.dmg.bcbm.logic.entity.bcbm;

/**
 * @author zhuqd
 * @Date 2017年9月15日
 * @Desc
 */
public class Banker {
	// 庄家id
	private String roleid;
	// 庄家昵称
	private String nickName;
	// 上庄次数
	private int count;
	// 庄家金币
	private double gold;
	// 预定上庄次数
	private int bankerCount;
	
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public double getGold() {
		return gold;
	}
	public void setGold(double gold) {
		this.gold = gold;
	}
	
	public int getBankerCount() {
		return bankerCount;
	}
	public void setBankerCount(int bankerCount) {
		this.bankerCount = bankerCount;
	}
	@Override
	public String toString() {
		return "Banker [roleid=" + roleid + ", nickName=" + nickName + ", count=" + count + ", gold=" + gold + "]";
	}
	
}
