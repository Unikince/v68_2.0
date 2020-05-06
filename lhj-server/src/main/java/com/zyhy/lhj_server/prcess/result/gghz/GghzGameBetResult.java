/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.gghz;

import java.util.List;

import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.gghz.GghzWinInfo;
import com.zyhy.lhj_server.game.gghz.GghzWindowInfo;

/**
 * @author ASUS
 *
 */
public class GghzGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<GghzWindowInfo> windowinfos;
	// 用户游戏币
	private double usercoin;
	// 奖励游戏币
	private double rewardcoin;
	//中奖线路
	private GghzWinInfo winrouteinfos;
	
	public List<GghzWindowInfo> getWindowinfos() {
		return windowinfos;
	}

	public void setWindowinfos(List<GghzWindowInfo> windowinfos) {
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

	public GghzWinInfo getWinrouteinfos() {
		return winrouteinfos;
	}

	public void setWinrouteinfos(GghzWinInfo winrouteinfos) {
		this.winrouteinfos = winrouteinfos;
	}

	@Override
	public String toString() {
		return "GghzGameBetResult [windowinfos=" + windowinfos + ", usercoin=" + usercoin + ", rewardcoin=" + rewardcoin
				+ ", winrouteinfos=" + winrouteinfos + ", ret=" + ret + ", msg=" + msg + ", roundId=" + roundId + "]";
	}
	
}
