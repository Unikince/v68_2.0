/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.yzhx;

import java.util.List;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.yzhx.YzhxFreeInfo;

public class YzhxGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<Window> windowinfos;
	//中奖线路
	private List<WinLineInfo> winrouteinfos;
	// 用户游戏币
	private double usercoin;
	// 正常游戏奖励
	private double rewardcoin;
	// 免费游戏奖励
	//private double freeRewardcoin;
	// 总奖励
	//private double totalRewardcoin;
	//免费游戏进行数据
	private YzhxFreeInfo freeInfo;
	//是否进行免费游戏
	private boolean isFree;
	//免费游戏次数
	private int freeNum;
	// wild的类型
	private List<Integer> wildType;
	
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
	public YzhxFreeInfo getFreeInfo() {
		return freeInfo;
	}
	public void setFreeInfo(YzhxFreeInfo freeInfo) {
		this.freeInfo = freeInfo;
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
	
	public List<Integer> getWildType() {
		return wildType;
	}
	public void setWildType(List<Integer> wildType) {
		this.wildType = wildType;
	}
	//public double getFreeRewardcoin() {
		//return freeRewardcoin;
	//}
	//public void setFreeRewardcoin(double freeRewardcoin) {
		//this.freeRewardcoin = freeRewardcoin;
	//}
	//public double getTotalRewardcoin() {
		//return this.freeRewardcoin + this.rewardcoin;
	//}
	
	@Override
	public String toString() {
		return "YzhxGameBetResult [windowinfos=" + windowinfos + ", winrouteinfos=" + winrouteinfos + ", usercoin="
				+ usercoin + ", rewardcoin=" + rewardcoin + ", freeInfo=" + freeInfo + ", isFree=" + isFree
				+ ", freeNum=" + freeNum + ", wildType=" + wildType + ", ret=" + ret + ", msg=" + msg + ", roundId="
				+ roundId + "]";
	}

}
