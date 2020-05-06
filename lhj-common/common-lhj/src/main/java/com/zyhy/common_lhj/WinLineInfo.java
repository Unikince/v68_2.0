/**
 * 
 */
package com.zyhy.common_lhj;

import java.util.List;

/**
 * @author ASUS
 *
 */
public class WinLineInfo {

	//线路id
	protected String id;
	//位置1
	protected List<Window> windows;
	//奖励
	protected double reward;
	//奖励类型 1奖金翻倍 2免费次数
	protected int rewardType;
	//有效图标数量
	protected int num;
	//线路顺序 1正序 2倒序
	protected int order = 1;
	//胜利的icon
	transient Icon icon;
	
	public double getReward() {
		return reward;
	}
	public void setReward(double reward) {
		this.reward = reward;
	}
	public int getRewardType() {
		return rewardType;
	}
	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	public List<Window> getWindows() {
		return windows;
	}
	public void setWindows(List<Window> windows) {
		this.windows = windows;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Icon getIcon() {
		return icon;
	}
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	@Override
	public String toString() {
		return "WinLineInfo [id=" + id + ", windows=" + windows + ", reward="
				+ reward + ", rewardType=" + rewardType + ", num=" + num
				+ ", order=" + order + ", icon=" + icon + "]";
	}
	
	public int validWailNum(){
		if(windows.size() == 0){
			return 0;
		}
		int n=0;
		for(int i=0; i<num; i++){
			Window w = null;
			if(order == 1){
				w = windows.get(i);
			}else{
				w = windows.get(windows.size() - i - 1);
			}
			if(w.getIcon().isWild()){
				n++;
			}
		}
		return n;
	}
}
