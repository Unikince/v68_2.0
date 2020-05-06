/**
 * 
 */
package com.zyhy.common_lhj;

/**
 * @author ASUS
 *
 */
public class Reward {

	public static final int TYPE_GOLD = 1;
	
	public static final int TYPE_FREENUM = 2;
	
	//1奖金 2免费次数
	private int type;
	
	private double reward;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}

	@Override
	public String toString() {
		return "RewardInfo [type=" + type + ", reward=" + reward + "]";
	}
	
}
