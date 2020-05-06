package com.dmg.bcbm.logic.entity.bcbm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.dmg.bcbm.logic.entity.SimplePlayer;
import com.dmg.gameconfigserverapi.dto.BairenFileConfigDTO;

public class RoomInfoResult {
	// 状态与时间
	private Map<String, Integer> time = new HashMap<>();
	// 下注信息K:车辆V车辆总下注
	private Map<Integer, Double> totalBetInfo;
	// 车辆下注信息K:车辆V车辆的每一次投注
	private Map<Integer, List<Integer>> carBetInfo;
	// 当前总下注金额
	private double totalBet;
	// 庄家信息
	private Banker banker;
	// 预定上庄次数
	private int bankerCount;
	// 下注筹码列表
	private List<String> betList;
	// 申请庄家列表
	private List<String> bankerList;
	// 游戏记录
	private List<BmbcWinInfo> gameRecord;
	// 是否开奖
	private boolean isEnd;
	// 玩家金币
	private double gold;
	// 是否需要等待
	private boolean wait;
	// 上一局的下注信息
	private Map<String, UserBetInfo> lastBetInfo;
	// 当前用户的上局下注信息
	private UserBetInfo lastUserBetInfo;
	// 当前用户的下注信息
	private UserBetInfo currentBetInfo;
	// 用户信息
	private SimplePlayer player;
	// 场次配置
	private BairenFileConfigDTO fileConfig;
	// 上一局游戏结果
	private mainGameProcessResult lastResult;
	public Map<String, Integer> getTime() {
		return time;
	}
	public void setTime(Map<String, Integer> time) {
		this.time = time;
	}
	public Banker getBanker() {
		return banker;
	}
	public void setBanker(Banker banker) {
		this.banker = banker;
	}
	public List<String> getBankerList() {
		return bankerList;
	}
	public void setBankerList(List<String> bankerList) {
		this.bankerList = bankerList;
	}
	public double getGold() {
		return gold;
	}
	public void setGold(double gold) {
		this.gold = gold;
	}
	public List<BmbcWinInfo> getGameRecord() {
		return gameRecord;
	}
	
	public List<String> getBetList() {
		return betList;
	}
	public void setBetList(List<String> betList) {
		this.betList = betList;
	}
	public void setGameRecord(List<BmbcWinInfo> gameRecord) {
		this.gameRecord = gameRecord;
	}
	public Map<Integer, Double> getTotalBetInfo() {
		return totalBetInfo;
	}
	
	public SimplePlayer getPlayer() {
		return player;
	}
	public void setPlayer(SimplePlayer player) {
		this.player = player;
	}
	public void setTotalBetInfo(Map<Integer, List<Double>> carBetInfo) {
		Map<Integer, Double> map = new HashMap<>();
		if (carBetInfo.size() > 0) {
			for (Integer car : carBetInfo.keySet()) {
				double temp =0;
				for (Double bet : carBetInfo.get(car)) {
					temp += bet;
				}
				map.put(car, temp);
			}
		}
		this.totalBetInfo = map;
	}
	public boolean isWait() {
		return wait;
	}
	public void setWait(boolean wait) {
		this.wait = wait;
	}
	public double getTotalBet() {
		return totalBet;
	}
	public void setTotalBet(double totalBet) {
		this.totalBet = totalBet;
	}
	public Map<Integer, List<Integer>> getCarBetInfo() {
		return carBetInfo;
	}
	public void setCarBetInfo(Map<Integer,List<Integer>> carBetInfo) {
		this.carBetInfo = carBetInfo;
	}
	
	public boolean isEnd() {
		return isEnd;
	}
	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
	
	public Map<String, UserBetInfo> getLastBetInfo() {
		return lastBetInfo;
	}
	public void setLastBetInfo(Map<String, UserBetInfo> lastBetInfo) {
		this.lastBetInfo = lastBetInfo;
	}
	
	public UserBetInfo getCurrentBetInfo() {
		return currentBetInfo;
	}
	public void setCurrentBetInfo(UserBetInfo currentBetInfo) {
		this.currentBetInfo = currentBetInfo;
	}
	public int getBankerCount() {
		return bankerCount;
	}
	public void setBankerCount(int bankerCount) {
		this.bankerCount = bankerCount;
	}
	public BairenFileConfigDTO getFileConfig() {
		return fileConfig;
	}
	public void setFileConfig(BairenFileConfigDTO fileConfig) {
		this.fileConfig = fileConfig;
	}
	
	public UserBetInfo getLastUserBetInfo() {
		return lastUserBetInfo;
	}
	public void setLastUserBetInfo(UserBetInfo lastUserBetInfo) {
		this.lastUserBetInfo = lastUserBetInfo;
	}
	
	public mainGameProcessResult getLastResult() {
		return lastResult;
	}
	public void setLastResult(mainGameProcessResult lastResult) {
		this.lastResult = lastResult;
	}
	@Override
	public String toString() {
		return "RoomInfoResult [time=" + time + ", totalBetInfo=" + totalBetInfo + ", carBetInfo=" + carBetInfo
				+ ", totalBet=" + totalBet + ", banker=" + banker + ", bankerCount=" + bankerCount + ", betList="
				+ betList + ", bankerList=" + bankerList + ", gameRecord=" + gameRecord + ", isEnd=" + isEnd + ", gold="
				+ gold + ", wait=" + wait + ", lastBetInfo=" + lastBetInfo + ", lastUserBetInfo=" + lastUserBetInfo
				+ ", currentBetInfo=" + currentBetInfo + ", player=" + player + ", fileConfig=" + fileConfig
				+ ", lastResult=" + lastResult + "]";
	}
	
}
