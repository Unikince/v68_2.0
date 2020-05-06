/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.swk;

import java.util.List;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.swk.SwkScatterInfo;

/**
 * @author ASUS
 *
 */
public class SwkGameBetResult extends HttpMessageResult{

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
	// scatter中奖
	private double scatterReward;
	// scatter中奖个数
	private int scatterCount;
	//是否进行免费游戏 
	private boolean Scatter;
	//免费游戏次数
	private int ScatterNum;
	//SCATTER免费游戏进行数据
	private SwkScatterInfo scatterInfo;
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
	public boolean isScatter() {
		return Scatter;
	}
	public void setScatter(boolean scatter) {
		Scatter = scatter;
	}
	public int getScatterNum() {
		return ScatterNum;
	}
	public void setScatterNum(int scatterNum) {
		ScatterNum = scatterNum;
	}
	public SwkScatterInfo getScatterInfo() {
		return scatterInfo;
	}
	public void setScatterInfo(SwkScatterInfo scatterInfo) {
		this.scatterInfo = scatterInfo;
	}
	public double getScatterReward() {
		return scatterReward;
	}
	public void setScatterReward(double scatterReward) {
		this.scatterReward = scatterReward;
	}
	
	public int getScatterCount() {
		return scatterCount;
	}
	public void setScatterCount(int scatterCount) {
		this.scatterCount = scatterCount;
	}
	
	@Override
	public String toString() {
		return "SwkGameBetResult [windowinfos=" + windowinfos + ", winrouteinfos=" + winrouteinfos + ", usercoin="
				+ usercoin + ", rewardcoin=" + rewardcoin + ", poolGame=" + poolGame + ", scatterReward="
				+ scatterReward + ", scatterCount=" + scatterCount + ", Scatter=" + Scatter + ", ScatterNum="
				+ ScatterNum + ", scatterInfo=" + scatterInfo + ", ret=" + ret + ", msg=" + msg + ", roundId=" + roundId
				+ "]";
	}

}
