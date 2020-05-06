/**
 * 
 */
package com.zyhy.lhj_server.prcess.result.fkmj;

import java.util.List;

import com.zyhy.common_lhj.WinLineInfo;
import com.zyhy.common_lhj.Window;
import com.zyhy.common_server.result.HttpMessageResult;
import com.zyhy.lhj_server.game.fkmj.FkmjBonusInfo;
import com.zyhy.lhj_server.game.fkmj.FkmjReplenish;

public class FkmjGameBetResult extends HttpMessageResult{

	//视窗信息
	private List<Window> windowinfos;
	//中奖线路
	private List<WinLineInfo> winrouteinfos;
	// 用户游戏币
	private double usercoin;
	// 游戏奖励
	private double rewardcoin;
	// 用于展示免费奖励
	private double freeRewardcoin;
	// 用于展示中免费时,主游戏奖励展示
	private double totalRewardcoin;
	//奖池游戏
	private int poolGame;
	//免费游戏进行数据
	private FkmjBonusInfo bonusInfo;
	//是否进行免费游戏
	private boolean isBonus;
	//免费游戏次数
	private int bonusNum;
	// 是否掉落
	private boolean isDrop;
	// 补充数据
	private FkmjReplenish rep;
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
	public FkmjBonusInfo getBonusInfo() {
		return bonusInfo;
	}
	public void setBonusInfo(FkmjBonusInfo bonusInfo) {
		this.bonusInfo = bonusInfo;
	}
	public boolean isBonus() {
		return isBonus;
	}
	public void setBonus(boolean isBonus) {
		this.isBonus = isBonus;
	}
	public int getBonusNum() {
		return bonusNum;
	}
	public void setBonusNum(int bonusNum) {
		this.bonusNum = bonusNum;
	}
	public boolean isDrop() {
		return isDrop;
	}
	public void setDrop(boolean isDrop) {
		this.isDrop = isDrop;
	}
	
	public FkmjReplenish getRep() {
		return rep;
	}
	public void setRep(FkmjReplenish rep) {
		this.rep = rep;
	}
	public void setTotalRewardcoin(double totalRewardcoin) {
		this.totalRewardcoin = totalRewardcoin;
	}
	
	public double getTotalRewardcoin() {
		return totalRewardcoin;
	}
	public double getFreeRewardcoin() {
		return freeRewardcoin;
	}
	public void setFreeRewardcoin(double freeRewardcoin) {
		this.freeRewardcoin = freeRewardcoin;
	}
	
	@Override
	public String toString() {
		return "FkmjGameBetResult [windowinfos=" + windowinfos + ", winrouteinfos=" + winrouteinfos + ", usercoin="
				+ usercoin + ", rewardcoin=" + rewardcoin + ", freeRewardcoin=" + freeRewardcoin + ", totalRewardcoin="
				+ totalRewardcoin + ", poolGame=" + poolGame + ", bonusInfo=" + bonusInfo + ", isBonus=" + isBonus
				+ ", bonusNum=" + bonusNum + ", isDrop=" + isDrop + ", rep=" + rep + ", ret=" + ret + ", msg=" + msg
				+ ", roundId=" + roundId + "]";
	}
	
	
}
