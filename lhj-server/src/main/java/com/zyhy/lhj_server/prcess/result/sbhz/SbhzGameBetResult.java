/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.sbhz;

import java.util.List;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.sbhz.SbhzWildMonKey;

/**
 * @author ASUS
 *
 */
public class SbhzGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<Window> windowinfos;
	//中奖线路
	private List<WinLineInfo> winrouteinfos;
	// 用户游戏币
	private double usercoin;
	// 是否是百搭猴子
	private boolean isMonkey1;
	// 是否需要重转
	private boolean isTurned;
	// 重转信息
	private SbhzWildMonKey monkey;
	// 不需要重转的列
	private List<Integer> wilds;
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
	
	public boolean isMonkey1() {
		return isMonkey1;
	}
	public void setMonkey1(boolean isMonkey1) {
		this.isMonkey1 = isMonkey1;
	}
	public boolean isTurned() {
		return isTurned;
	}
	public void setTurned(boolean isTurned) {
		this.isTurned = isTurned;
	}
	public List<Integer> getWilds() {
		return wilds;
	}
	public void setWilds(List<Integer> wilds) {
		this.wilds = wilds;
	}
	
	public SbhzWildMonKey getMonkey() {
		return monkey;
	}
	public void setMonkey(SbhzWildMonKey monkey) {
		this.monkey = monkey;
	}
	
	@Override
	public String toString() {
		return "SbhzGameBetResult [windowinfos=" + windowinfos + ", winrouteinfos=" + winrouteinfos + ", usercoin="
				+ usercoin + ", isMonkey1=" + isMonkey1 + ", isTurned=" + isTurned + ", monkey=" + monkey + ", wilds="
				+ wilds + ", rewardcoin=" + rewardcoin + ", poolGame=" + poolGame + ", ret=" + ret + ", msg=" + msg
				+ ", roundId=" + roundId + "]";
	}
	
}
