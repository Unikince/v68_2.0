/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.ajxz;

import java.util.List;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.ajxz.AjxzFreeInfo;

public class AjxzGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<Window> windowinfos;
	//中奖线路
	private List<WinLineInfo> winrouteinfos;
	// 用户游戏币
	private double usercoin;
	// 正常游戏奖励
	private double rewardcoin;
	//免费游戏进行数据
	private AjxzFreeInfo freeInfo;
	//是否进行免费游戏
	private boolean isFree;
	// 是否保存补充数据
	private boolean isReplenish;
	//免费游戏次数
	private int freeNum;
	
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
	public AjxzFreeInfo getFreeInfo() {
		return freeInfo;
	}
	public void setFreeInfo(AjxzFreeInfo freeInfo) {
		this.freeInfo = freeInfo;
	}
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	
	public boolean isReplenish() {
		return isReplenish;
	}
	public void setReplenish(boolean isReplenish) {
		this.isReplenish = isReplenish;
	}
	public int getFreeNum() {
		return freeNum;
	}
	public void setFreeNum(int freeNum) {
		this.freeNum = freeNum;
	}
	@Override
	public String toString() {
		return "AjxzGameBetResult [windowinfos=" + windowinfos + ", winrouteinfos=" + winrouteinfos + ", usercoin="
				+ usercoin + ", rewardcoin=" + rewardcoin + ", freeInfo=" + freeInfo + ", isFree=" + isFree
				+ ", isReplenish=" + isReplenish + ", freeNum=" + freeNum + ", ret=" + ret + ", msg=" + msg
				+ ", roundId=" + roundId + "]";
	}

}
