package com.dmg.bcbm.logic.entity.bcbm;

import java.util.List;

/**
 * @author zhuqd
 * @Date 2017年9月15日
 * @Desc
 */
public class BankerListInfo {
	// 当前庄家
	private String roleId;
	// 是否为庄家 0不是,1是
	private int banker;
	// 是否在队列中 0不在,1在
	private int queue;
	// 当前排第几位
	private int num;
	// 庄家列表
	private List<String> bankerList;
	// 预定上庄次数
	private int bankerCount;
	public int getBanker() {
		return banker;
	}
	public void setBanker(int banker) {
		this.banker = banker;
	}
	public int getQueue() {
		return queue;
	}
	public void setQueue(int queue) {
		this.queue = queue;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public List<String> getBankerList() {
		return bankerList;
	}
	public void setBankerList(List<String> bankerList) {
		this.bankerList = bankerList;
	}
	
	public int getBankerCount() {
		return bankerCount;
	}
	public void setBankerCount(int bankerCount) {
		this.bankerCount = bankerCount;
	}
	@Override
	public String toString() {
		return "BankerListInfo [roleId=" + roleId + ", banker=" + banker + ", queue=" + queue + ", num=" + num
				+ ", bankerList=" + bankerList + ", bankerCount=" + bankerCount + "]";
	}
	
}