package com.zyhy.lhj_server.game;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zyhy.common_lhj.BetInfo;
import com.zyhy.common_server.result.HttpMessageResult;


public class ShowRecordResult {
	// 游戏名称
	private String gameName;
	// 记录类型
	private String recordType;
	// 代码
	private String dbId; 
	// 日期
	private String date;
	// 开始时余额
	private double startbalance ;
	// 投注
	private double bet  ;
	// 投注信息
	private BetInfo betInfo;
	// 奖励
	private double reward   ;
	// 结束时余额
	private double endbalance  ;
	// 游戏结果
	private HttpMessageResult gameresult   ;
	// 游戏时间
	private long gametime ;
	// 玩家昵称
	private String rolenick  ;
	// 游戏回合id
	private String roundId  ;
	// 中奖区间
	private double[] RewardBetween;
	// 今日输赢
	private double todayWin;
	// 累计输赢
	private double totalWin;
	// 记录展示类型,1x3,3x3,3x5,4x4,5x5,6x6
	private int viewType;
	
	private List<String> windows; // 用于后台显示的窗口信息
	private Map<String, Double> winLine; // 用于后台显示的线路信息
	
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getDbId() {
		return dbId;
	}
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getStartbalance() {
		return startbalance;
	}
	public void setStartbalance(double startbalance) {
		this.startbalance = startbalance;
	}
	public double getBet() {
		return bet;
	}
	public void setBet(double bet) {
		this.bet = bet;
	}
	
	public BetInfo getBetInfo() {
		return betInfo;
	}
	public void setBetInfo(BetInfo betInfo) {
		this.betInfo = betInfo;
	}
	public double getReward() {
		return reward;
	}
	public void setReward(double reward) {
		this.reward = reward;
	}
	public double getEndbalance() {
		return endbalance;
	}
	public void setEndbalance(double endbalance) {
		this.endbalance = endbalance;
	}
	
	public HttpMessageResult getGameresult() {
		return gameresult;
	}
	public void setGameresult(HttpMessageResult gameresult) {
		this.gameresult = gameresult;
	}
	
	public long getGametime() {
		return gametime;
	}
	public void setGametime(long gametime) {
		this.gametime = gametime;
	}
	public String getRolenick() {
		return rolenick;
	}
	public void setRolenick(String rolenick) {
		this.rolenick = rolenick;
	}
	
	public String getRoundId() {
		return roundId;
	}
	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}
	
	public String getRecordType() {
		return recordType;
	}
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}
	
	public double[] getRewardBetween() {
		return RewardBetween;
	}
	public void setRewardBetween(double[] rewardBetween) {
		RewardBetween = rewardBetween;
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
	public List<String> getWindows() {
		return windows;
	}
	public void setWindows(List<String> windows) {
		this.windows = windows;
	}
	public Map<String, Double> getWinLine() {
		return winLine;
	}
	public void setWinLine(Map<String, Double> winLine) {
		this.winLine = winLine;
	}
	
	public int getViewType() {
		return viewType;
	}
	public void setViewType(int viewType) {
		this.viewType = viewType;
	}
	
	@Override
	public String toString() {
		return "ShowRecordResult [gameName=" + gameName + ", recordType=" + recordType + ", dbId=" + dbId + ", date="
				+ date + ", startbalance=" + startbalance + ", bet=" + bet + ", betInfo=" + betInfo + ", reward="
				+ reward + ", endbalance=" + endbalance + ", gameresult=" + gameresult + ", gametime=" + gametime
				+ ", rolenick=" + rolenick + ", roundId=" + roundId + ", RewardBetween="
				+ Arrays.toString(RewardBetween) + ", todayWin=" + todayWin + ", totalWin=" + totalWin + ", viewType="
				+ viewType + ", windows=" + windows + ", winLine=" + winLine + "]";
	}
	
}
