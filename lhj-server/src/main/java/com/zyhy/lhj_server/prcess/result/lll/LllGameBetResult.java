/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.lll;

import java.util.List;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.lll.LllWinInfo;
import com.zyhy.lhj_server.game.lll.LllWindowInfo;

/**
 * @author ASUS
 *
 */
public class LllGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<LllWindowInfo> windowinfos;
	// 用户游戏币
	private double usercoin;
	// 奖励游戏币
	private double rewardcoin;
	//中奖线路
	private LllWinInfo winrouteinfos;
	
	public List<LllWindowInfo> getWindowinfos() {
		return windowinfos;
	}

	public void setWindowinfos(List<LllWindowInfo> windowinfos) {
		this.windowinfos = windowinfos;
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

	public LllWinInfo getWinrouteinfos() {
		return winrouteinfos;
	}

	public void setWinrouteinfos(LllWinInfo winrouteinfos) {
		this.winrouteinfos = winrouteinfos;
	}

	@Override
	public String toString() {
		return "LllGameBetResult [windowinfos=" + windowinfos + ", usercoin=" + usercoin + ", rewardcoin=" + rewardcoin
				+ ", winrouteinfos=" + winrouteinfos + ", ret=" + ret + ", msg=" + msg + ", roundId=" + roundId + "]";
	}
	
}
