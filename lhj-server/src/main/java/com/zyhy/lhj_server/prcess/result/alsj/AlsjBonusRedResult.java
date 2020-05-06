/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.alsj;

import java.util.List;

import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author ASUS
 *
 */
public class AlsjBonusRedResult extends HttpMessageResult{
	// 用户游戏币
	private double usercoin;
	
	// 奖励游戏币
	private List<Double> rewardcoin;
	
	// 奖励的倍数
	private List<Integer> num;
	
	// 总奖励游戏币
	private double rewardAllCoin;
	

	public double getUsercoin() {
		return usercoin;
	}

	public void setUsercoin(double usercoin) {
		this.usercoin = usercoin;
	}

	public List<Double> getRewardcoin() {
		return rewardcoin;
	}

	public void setRewardcoin(List<Double> rewardcoin) {
		this.rewardcoin = rewardcoin;
	}

	public double getRewardAllCoin() {
		return rewardAllCoin;
	}

	public void setRewardAllCoin(double rewardAllCoin) {
		this.rewardAllCoin = rewardAllCoin;
	}

	public List<Integer> getNum() {
		return num;
	}

	public void setNum(List<Integer> num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "AlsjBonusRedResult [usercoin=" + usercoin + ", rewardcoin=" + rewardcoin + ", num=" + num
				+ ", rewardAllCoin=" + rewardAllCoin + ", ret=" + ret + ", msg=" + msg + ", roundId=" + roundId + "]";
	}


}
