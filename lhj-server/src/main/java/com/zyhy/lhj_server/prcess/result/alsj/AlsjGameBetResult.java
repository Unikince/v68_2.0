/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.alsj;

import java.util.List;

import com.zyhy.common_lhj.WindowInfo;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.RewardInfo;
import com.zyhy.lhj_server.game.alsj.AlsjBonusInfo;
import com.zyhy.lhj_server.game.alsj.AlsjScatterInfo;

/**
 * @author ASUS
 *
 */
public class AlsjGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<List<WindowInfo>> windowinfos;
	//获奖视窗信息
	private List<List<WindowInfo>> win;
	// 获奖线路信息
	private List<RewardInfo> rewardInfo;
	// 用户游戏币
	private double usercoin;
	// 奖励游戏币
	private double rewardcoin;
	//奖池游戏
	private int poolGame;
	//bonus游戏信息
	private boolean bonus;
	//bonus戏进行数据
	private AlsjBonusInfo bonusInfo;
	//随机全是WILD
	private int isWild;
	//是否进行免费游戏 
	private boolean Scatter;
	//免费游戏次数
	private int ScatterNum;
	//SCATTER免费游戏进行数据
	private AlsjScatterInfo scatterInfo;
	// scatter图标中奖
	private double scattericon;
	// scatter图标中奖数量
	private int scattericonnum;
	// bonus图标中奖
	private double bonusicon;
	// bonus图标中奖数量
	private int bonusiconnum;
	// wild图标中奖
	private double wildicon;
	// wild图标中奖数量
	private int wildiconnum;
	
	
	
	public double getScattericon() {
		return scattericon;
	}
	public void setScattericon(double scattericon) {
		this.scattericon = scattericon;
	}
	public double getBonusicon() {
		return bonusicon;
	}
	public void setBonusicon(double bonusicon) {
		this.bonusicon = bonusicon;
	}
	public double getWildicon() {
		return wildicon;
	}
	public void setWildicon(double wildicon) {
		this.wildicon = wildicon;
	}
	public int getScattericonnum() {
		return scattericonnum;
	}
	public void setScattericonnum(int scattericonnum) {
		this.scattericonnum = scattericonnum;
	}
	public int getBonusiconnum() {
		return bonusiconnum;
	}
	public void setBonusiconnum(int bonusiconnum) {
		this.bonusiconnum = bonusiconnum;
	}
	public int getWildiconnum() {
		return wildiconnum;
	}
	public void setWildiconnum(int wildiconnum) {
		this.wildiconnum = wildiconnum;
	}
	public List<List<WindowInfo>> getWindowinfos() {
		return windowinfos;
	}
	public void setWindowinfos(List<List<WindowInfo>> windowinfos) {
		this.windowinfos = windowinfos;
	}
	public List<List<WindowInfo>> getWin() {
		return win;
	}
	public void setWin(List<List<WindowInfo>> win) {
		this.win = win;
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
	public AlsjBonusInfo getAlsjBonusInfo() {
		return bonusInfo;
	}
	public void setAlsjBonusInfo(AlsjBonusInfo bonusInfo) {
		this.bonusInfo = bonusInfo;
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
	public AlsjScatterInfo getAlsjScatterInfo() {
		return scatterInfo;
	}
	public void setAlsjScatterInfo(AlsjScatterInfo scatterInfo) {
		this.scatterInfo = scatterInfo;
	}
	public int getIsWild() {
		return isWild;
	}
	public void setIsWild(int isWild) {
		this.isWild = isWild;
	}
	public boolean isBonus() {
		return bonus;
	}
	public void setBonus(boolean bonus) {
		this.bonus = bonus;
	}
	public List<RewardInfo> getRewardInfo() {
		return rewardInfo;
	}
	public void setRewardInfo(List<RewardInfo> rewardInfo) {
		this.rewardInfo = rewardInfo;
	}
	
	@Override
	public String toString() {
		return "AlsjGameBetResult [windowinfos=" + windowinfos + ", win=" + win + ", rewardInfo=" + rewardInfo
				+ ", usercoin=" + usercoin + ", rewardcoin=" + rewardcoin + ", poolGame=" + poolGame + ", bonus="
				+ bonus + ", bonusInfo=" + bonusInfo + ", isWild=" + isWild + ", Scatter=" + Scatter + ", ScatterNum="
				+ ScatterNum + ", scatterInfo=" + scatterInfo + ", scattericon=" + scattericon + ", scattericonnum="
				+ scattericonnum + ", bonusicon=" + bonusicon + ", bonusiconnum=" + bonusiconnum + ", wildicon="
				+ wildicon + ", wildiconnum=" + wildiconnum + ", ret=" + ret + ", msg=" + msg + ", roundId=" + roundId
				+ "]";
	}
	
}
