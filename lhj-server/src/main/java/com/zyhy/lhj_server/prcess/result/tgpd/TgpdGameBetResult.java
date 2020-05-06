/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.tgpd;

import java.util.List;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.tgpd.TgpdDorpInfo;
import com.zyhy.lhj_server.game.tgpd.TgpdReplenish;
import com.zyhy.lhj_server.game.tgpd.TgpdScatterInfo;
import com.zyhy.lhj_server.game.tgpd.WindowInfo;

public class TgpdGameBetResult extends HttpMessageResult{

	//游戏视窗信息
	private List<List<WindowInfo>> windowinfos;
	//掉落视窗信息
	private List<List<WindowInfo>> dropWindowInfos;
	//中奖图标
	private List<List<WindowInfo>> winrouteinfos;
	// 中奖线路
	private List<WinLineInfo> winLine;
	// 用户游戏币
	private double usercoin;
	// 正常游戏奖励
	private double rewardcoin;
	// 是否有奖池奖励
	private boolean isPoolReward;
	// 奖池奖励级别
	private int poolCount;
	// 奖池奖励
	private double poolRewardcoin;
	//是否进行免费游戏
	private boolean isFree;
	//免费游戏次数
	private int freeNum;
	// 免费游戏奖励倍数
	private int mul;
	// 免费游戏信息
	private TgpdScatterInfo scatter;
	// 是否保存补充数据
	private boolean isReplenish;
	// 补充数据信息
	private TgpdReplenish repInfo;
	// 道具数量
	private int propsNum;
	// 当前模式
	private int type;
	// 掉落窗口信息
	private TgpdDorpInfo tgpdDorpInfo;
	
	// TODO 测试用字段
	// 当前赔率
	private double odds;
	// 当前使用图标
	private int icon;
	// 当前总下注
	private double totalBet;
	// 当前总派彩
	private double totalReward;
	
	
	public List<List<WindowInfo>> getWindowinfos() {
		return windowinfos;
	}
	public void setWindowinfos(List<List<WindowInfo>> windowinfos) {
		this.windowinfos = windowinfos;
	}
	public List<List<WindowInfo>> getDropWindowInfos() {
		return dropWindowInfos;
	}
	public void setDropWindowInfos(List<List<WindowInfo>> dropWindowInfos) {
		this.dropWindowInfos = dropWindowInfos;
	}
	public List<List<WindowInfo>> getWinrouteinfos() {
		return winrouteinfos;
	}
	public void setWinrouteinfos(List<List<WindowInfo>> winrouteinfos) {
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
	public boolean isPoolReward() {
		return isPoolReward;
	}
	public void setPoolReward(boolean isPoolReward) {
		this.isPoolReward = isPoolReward;
	}
	public int getPoolCount() {
		return poolCount;
	}
	public void setPoolCount(int poolCount) {
		this.poolCount = poolCount;
	}
	public double getPoolRewardcoin() {
		return poolRewardcoin;
	}
	public void setPoolRewardcoin(double poolRewardcoin) {
		this.poolRewardcoin = poolRewardcoin;
	}
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	public int getFreeNum() {
		return freeNum;
	}
	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}
	public TgpdScatterInfo getScatter() {
		return scatter;
	}
	public void setScatter(TgpdScatterInfo scatter) {
		this.scatter = scatter;
	}
	public boolean isReplenish() {
		return isReplenish;
	}
	public void setReplenish(boolean isReplenish) {
		this.isReplenish = isReplenish;
	}
	public TgpdReplenish getRepInfo() {
		return repInfo;
	}
	public void setRepInfo(TgpdReplenish repInfo) {
		this.repInfo = repInfo;
	}
	public int getPropsNum() {
		return propsNum;
	}
	public void setPropsNum(int propsNum) {
		this.propsNum = propsNum;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getMul() {
		return mul;
	}
	public void setMul(int mul) {
		this.mul = mul;
	}
	public List<WinLineInfo> getWinLine() {
		return winLine;
	}
	public void setWinLine(List<WinLineInfo> winLine) {
		this.winLine = winLine;
	}
	public double getOdds() {
		return odds;
	}
	public void setOdds(double odds) {
		this.odds = odds;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	public TgpdDorpInfo getTgpdDorpInfo() {
		return tgpdDorpInfo;
	}
	public void setTgpdDorpInfo(TgpdDorpInfo tgpdDorpInfo) {
		this.tgpdDorpInfo = tgpdDorpInfo;
	}
	
	public double getTotalBet() {
		return totalBet;
	}
	public void setTotalBet(double totalBet) {
		this.totalBet = totalBet;
	}
	public double getTotalReward() {
		return totalReward;
	}
	public void setTotalReward(double totalReward) {
		this.totalReward = totalReward;
	}
	
	@Override
	public String toString() {
		return "TgpdGameBetResult [windowinfos=" + windowinfos + ", dropWindowInfos=" + dropWindowInfos
				+ ", winrouteinfos=" + winrouteinfos + ", winLine=" + winLine + ", usercoin=" + usercoin
				+ ", rewardcoin=" + rewardcoin + ", isPoolReward=" + isPoolReward + ", poolCount=" + poolCount
				+ ", poolRewardcoin=" + poolRewardcoin + ", isFree=" + isFree + ", freeNum=" + freeNum + ", mul=" + mul
				+ ", scatter=" + scatter + ", isReplenish=" + isReplenish + ", repInfo=" + repInfo + ", propsNum="
				+ propsNum + ", type=" + type + ", tgpdDorpInfo=" + tgpdDorpInfo + ", odds=" + odds + ", icon=" + icon
				+ ", totalBet=" + totalBet + ", totalReward=" + totalReward + ", ret=" + ret + ", msg=" + msg
				+ ", roundId=" + roundId + "]";
	}
	
}
