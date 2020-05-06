/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.czdbz;

import java.util.List;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;

/**
 * @author ASUS
 *
 */
public class CzdbzGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<Window> windowinfos;
	//中奖线路
	private List<WinLineInfo> winrouteinfos;
	// 用户游戏币
	private double usercoin;
	// 奖励游戏币
	private double rewardcoin;
	//奖池游戏
	private int poolGame;
	public List<Window> getWindowinfos() {
		return windowinfos;
	}
	public void setWindowinfos(List<Window> windowinfos) {
		this.windowinfos = windowinfos;
	}
	public List<WinLineInfo> getWinrouteinfos() {
		return winrouteinfos;
	}
	public void setWinrouteinfos(List<WinLineInfo> winrouteinfos) {
		this.winrouteinfos = winrouteinfos;
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
	public int getPoolGame() {
		return poolGame;
	}
	public void setPoolGame(int poolGame) {
		this.poolGame = poolGame;
	}
	
	@Override
	public String toString() {
		return "CzdbzGameBetResult [windowinfos=" + windowinfos + ", winrouteinfos=" + winrouteinfos + ", usercoin="
				+ usercoin + ", rewardcoin=" + rewardcoin + ", poolGame=" + poolGame + ", ret=" + ret + ", msg=" + msg
				+ ", roundId=" + roundId + "]";
	}

}
