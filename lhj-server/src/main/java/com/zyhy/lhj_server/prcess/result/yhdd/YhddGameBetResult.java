/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.yhdd;

import java.util.List;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.yhdd.YhddBonusInfo;

/**
 * @author ASUS
 *
 */
public class YhddGameBetResult extends HttpMessageResult{

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
	//bonus游戏信息 1是 0否
	private boolean bonus;
	//bonus免费游戏进行数据
	private YhddBonusInfo bonusInfo;
	// scatter中奖
	private double scatterReward;
	
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
	public YhddBonusInfo getYhddBonusInfo() {
		return bonusInfo;
	}
	public void setYhddBonusInfo(YhddBonusInfo bonusInfo) {
		this.bonusInfo = bonusInfo;
	}
	public boolean isBonus() {
		return bonus;
	}
	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}
	
	public YhddBonusInfo getBonusInfo() {
		return bonusInfo;
	}
	public void setBonusInfo(YhddBonusInfo bonusInfo) {
		this.bonusInfo = bonusInfo;
	}
	public double getScatterReward() {
		return scatterReward;
	}
	public void setScatterReward(double scatterReward) {
		this.scatterReward = scatterReward;
	}
	
	@Override
	public String toString() {
		return "YhddGameBetResult [windowinfos=" + windowinfos + ", winrouteinfos=" + winrouteinfos + ", usercoin="
				+ usercoin + ", rewardcoin=" + rewardcoin + ", poolGame=" + poolGame + ", bonus=" + bonus
				+ ", bonusInfo=" + bonusInfo + ", scatterReward=" + scatterReward + ", ret=" + ret + ", msg=" + msg
				+ ", roundId=" + roundId + "]";
	}

}
