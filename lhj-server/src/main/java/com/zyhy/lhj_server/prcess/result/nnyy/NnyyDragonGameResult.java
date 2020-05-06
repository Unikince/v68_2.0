/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.nnyy;

import com.zyhy.common_lhj.pool.DragonGameData;
import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author ASUS
 *
 */
public class NnyyDragonGameResult extends HttpMessageResult{

	private DragonGameData dragonGameData;
	
	// 用户游戏币
	private double usercoin;
	// 奖励游戏币
	private double rewardcoin;
	// 1进行中  2结束
	private int status = 1;
	
	private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "NnyyDragonGameResult [dragonGameData=" + dragonGameData + ", usercoin=" + usercoin + ", rewardcoin="
				+ rewardcoin + ", status=" + status + ", name=" + name + ", ret=" + ret + ", msg=" + msg + ", roundId="
				+ roundId + "]";
	}

}
