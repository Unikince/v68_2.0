/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.lqjx;

import java.util.ArrayList;
import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.RewardInfo;
import com.zyhy.lhj_server.game.lqjx.LqjxReplenish;
import com.zyhy.lhj_server.game.lqjx.LqjxScatterInfo;

/**
 * @author ASUS
 *
 */
public class LqjxGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<List<WindowInfo>> windowinfos;
	//获奖视窗信息
	private List<List<WindowInfo>> win;
	//获胜线路信息
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
	//是否需要补充数据
	private boolean isReplenish;
	// 补充数据
	private LqjxReplenish rep;
	//SCATTER免费游戏进行数据
	private LqjxScatterInfo scatterInfo;
	//第234列随机全是WILD
	private int isWild;
	
	//第345列随机全是WILD
	private List<Integer> god = new ArrayList<Integer>();
	// scatter图标中奖
	private double scattericon;
	// scatter图标中奖数量
	private int scattericonnum;
	// 是否奖励加倍
	private int mul;
	// 加倍倍数
	private int rewardmul;
	
	
	public double getScattericon() {
		return scattericon;
	}
	public void setScattericon(double scattericon) {
		this.scattericon = scattericon;
	}
	public int getScattericonnum() {
		return scattericonnum;
	}
	public void setScattericonnum(int scattericonnum) {
		this.scattericonnum = scattericonnum;
	}
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
	public LqjxScatterInfo getLqjxScatterInfo() {
		return scatterInfo;
	}
	public void setLqjxScatterInfo(LqjxScatterInfo scatterInfo) {
		this.scatterInfo = scatterInfo;
	}
	public boolean isReplenish() {
		return isReplenish;
	}
	public void setReplenish(boolean isReplenish) {
		this.isReplenish = isReplenish;
	}
	
	public LqjxReplenish getRep() {
		return rep;
	}
	public void setRep(LqjxReplenish rep) {
		this.rep = rep;
	}
	public LqjxScatterInfo getScatterInfo() {
		return scatterInfo;
	}
	public void setScatterInfo(LqjxScatterInfo scatterInfo) {
		this.scatterInfo = scatterInfo;
	}
	public int getScatterNum() {
		return ScatterNum;
	}
	public void setScatterNum(int scatterNum) {
		ScatterNum = scatterNum;
	}
	public int getIsWild() {
		return isWild;
	}
	public void setIsWild(int isWild) {
		this.isWild = isWild;
	}
	public List<Integer> getGod() {
		return god;
	}
	public void setGod(List<Integer> god) {
		this.god = god;
	}
	public List<RewardInfo> getRewardInfo() {
		return rewardInfo;
	}
	public void setRewardInfo(List<RewardInfo> rewardInfo) {
		this.rewardInfo = rewardInfo;
	}
	public int getMul() {
		return mul;
	}
	public void setMul(int mul) {
		this.mul = mul;
	}
	public int getRewardmul() {
		return rewardmul;
	}
	public void setRewardmul(int rewardmul) {
		this.rewardmul = rewardmul;
	}
	@Override
	public String toString() {
		return "LqjxGameBetResult [windowinfos=" + windowinfos + ", win=" + win + ", rewardInfo=" + rewardInfo
				+ ", usercoin=" + usercoin + ", rewardcoin=" + rewardcoin + ", poolGame=" + poolGame + ", Scatter="
				+ Scatter + ", ScatterNum=" + ScatterNum + ", isReplenish=" + isReplenish + ", rep=" + rep
				+ ", scatterInfo=" + scatterInfo + ", isWild=" + isWild + ", god=" + god + ", scattericon="
				+ scattericon + ", scattericonnum=" + scattericonnum + ", mul=" + mul + ", rewardmul=" + rewardmul
				+ ", ret=" + ret + ", msg=" + msg + ", roundId=" + roundId + "]";
	}
	
}
