/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.zctz;

import com.zyhy.common_lhj.pool.DragonGameData;
import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author ASUS
 *
 */
public class ZctzDragonGameResult extends HttpMessageResult{

	private DragonGameData dragonGameData;
	
	// 用户游戏币
	private double usercoin;
	// 奖励游戏币
	private double rewardcoin;
	
	/**
	 * 1进行中  2结束
	 */
	private int status = 1;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public DragonGameData getDragonGameData() {
		return dragonGameData;
	}

	public void setDragonGameData(DragonGameData dragonGameData) {
		this.dragonGameData = dragonGameData;
	}

	public double getUsercoin() {
		return usercoin;
	}

	public void setUsercoin(double usercoin) {
		this.usercoin = usercoin;
	}

	public double getRewardcoin() {
		return rewardcoin;
	}

	public void setRewardcoin(double rewardcoin) {
		this.rewardcoin = rewardcoin;
	}

	@Override
	public String toString() {
		return "ZctzDragonGameResult [dragonGameData=" + dragonGameData
				+ ", usercoin=" + usercoin + ", rewardcoin=" + rewardcoin
				+ ", status=" + status + "]";
	}

}
