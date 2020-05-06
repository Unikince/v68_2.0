/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.hjws;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.RewardInfo;
import com.zyhy.lhj_server.game.hjws.HjwsScatterInfo;

/**
 * @author ASUS
 *
 */
public class HjwsGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<List<WindowInfo>> windowinfos;
	//获奖视窗信息
	private List<List<WindowInfo>> win;
	//获奖线路信息
	private List<RewardInfo> rewardInfo;
	// 用户游戏币
	private double usercoin;
	// 奖励游戏币
	private double rewardcoin;
	//奖池游戏
	private int poolGame;
	//是否进行免费游戏 
	private boolean Scatter;
	//免费游戏次数
	private int ScatterNum;
	//触发吸血蝙蝠倍数
	private int bat;
	//是否需要补充数据
	private boolean isAgain;
	//SCATTER免费游戏进行数据
	private HjwsScatterInfo scatterInfo;
	
	private int freeNum;
	
	private boolean isSpecial;
	// 特殊奖励金额
	private double specialRward;
	//第345列随机全是WILD
	private List<Integer> god = new ArrayList<Integer>();
	
	public List<List<WindowInfo>> getWindowinfos() {
		return windowinfos;
	}
	public void setWindowinfos(List<List<WindowInfo>> list) {
		this.windowinfos = list;
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
	
	public List<List<WindowInfo>> getWin() {
		return win;
	}
	public void setWin(List<List<WindowInfo>> win) {
		this.win = win;
	}
	
	public boolean isScatter() {
		return Scatter;
	}
	public void setScatter(boolean scatter) {
		Scatter = scatter;
	}
	public HjwsScatterInfo getScatterInfo() {
		return scatterInfo;
	}
	public void setScatterInfo(HjwsScatterInfo scatterInfo) {
		this.scatterInfo = scatterInfo;
	}
	public boolean isAgain() {
		return isAgain;
	}
	public void setAgain(boolean isAgain) {
		this.isAgain = isAgain;
	}
	public int getScatterNum() {
		return ScatterNum;
	}
	public void setScatterNum(int scatterNum) {
		ScatterNum = scatterNum;
	}
	public List<Integer> getGod() {
		return god;
	}
	public void setGod(List<Integer> god) {
		this.god = god;
	}
	public int getBat() {
		return bat;
	}
	public void setBat(int bat) {
		this.bat = bat;
	}
	public int getFreeNum() {
		return freeNum;
	}
	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}
	
	public boolean isSpecial() {
		return isSpecial;
	}
	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}
	public List<RewardInfo> getRewardInfo() {
		return rewardInfo;
	}
	public void setRewardInfo(List<RewardInfo> rewardInfo) {
		this.rewardInfo = rewardInfo;
	}
	public double getSpecialRward() {
		return specialRward;
	}
	public void setSpecialRward(double specialRward) {
		this.specialRward = specialRward;
	}
	
	@Override
	public String toString() {
		return "HjwsGameBetResult [windowinfos=" + windowinfos + ", win=" + win + ", rewardInfo=" + rewardInfo
				+ ", usercoin=" + usercoin + ", rewardcoin=" + rewardcoin + ", poolGame=" + poolGame + ", Scatter="
				+ Scatter + ", ScatterNum=" + ScatterNum + ", bat=" + bat + ", isAgain=" + isAgain + ", scatterInfo="
				+ scatterInfo + ", freeNum=" + freeNum + ", isSpecial=" + isSpecial + ", specialRward=" + specialRward
				+ ", god=" + god + ", ret=" + ret + ", msg=" + msg + ", roundId=" + roundId + "]";
	}

}
