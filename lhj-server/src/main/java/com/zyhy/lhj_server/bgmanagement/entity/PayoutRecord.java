package com.zyhy.lhj_server.bgmanagement.entity;

import java.util.Arrays;

/**
 * 派奖记录
 * @author Administrator
 *
 */
public class PayoutRecord {
	// 序号id
	private int id;
	// 派奖时间
	private long time;
	// 派奖时间
	private String formatTime;
	// 玩家id
	private int playerId;
	// 玩家昵称
	private String nickName;
	// 获奖金额
	private double reward;
	// 获奖区间
	private double[] rewardBetwen;
	// 获奖类型
	private int rewardType;
	// 获奖游戏名称
	private String rewardName;
	// 今日输赢
	private double todayWin;
	// 累计输赢
	private double totalWin;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getFormatTime() {
		return formatTime;
	}
	public void setFormatTime(String formatTime) {
		this.formatTime = formatTime;
	}
	public int getPlayerId() {
		return playerId;
	}
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public double getReward() {
		return reward;
	}
	public void setReward(double reward) {
		this.reward = reward;
	}
	
	public double[] getRewardBetwen() {
		return rewardBetwen;
	}
	public void setRewardBetwen(double[] rewardBetwen) {
		this.rewardBetwen = rewardBetwen;
	}
	public int getRewardType() {
		return rewardType;
	}
	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}
	public String getRewardName() {
		return rewardName;
	}
	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}
	public double getTodayWin() {
		return todayWin;
	}
	public void setTodayWin(double todayWin) {
		this.todayWin = todayWin;
	}
	public double getTotalWin() {
		return totalWin;
	}
	public void setTotalWin(double totalWin) {
		this.totalWin = totalWin;
	}
	@Override
	public String toString() {
		return "PayoutRecord [id=" + id + ", time=" + time + ", formatTime=" + formatTime + ", playerId=" + playerId
				+ ", nickName=" + nickName + ", reward=" + reward + ", rewardBetwen=" + Arrays.toString(rewardBetwen)
				+ ", rewardType=" + rewardType + ", rewardName=" + rewardName + ", todayWin=" + todayWin + ", totalWin="
				+ totalWin + "]";
	}
	
	
}
