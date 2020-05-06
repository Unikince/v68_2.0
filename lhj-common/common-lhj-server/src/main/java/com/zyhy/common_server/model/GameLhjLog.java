/**
 * 
 */
package com.zyhy.common_server.model;

import java.util.Arrays;
import java.util.Date;

/**
 * @author linanjun
 *
 */
public class GameLhjLog{

	private int id;
	private String logtype;
	private String gamename;
	private String roleid;
	// 玩家昵称
	private String rolenick  ;
	private String uuid;
	// 下注
	private double bet;
	// 奖励
	private double reward;
	// 中奖区间
	private double[] RewardBetween;
	// 今日输赢
	private double todayWin;
	// 累计输赢
	private double totalWin;
	// 奖励类型
	private int rewardtype;
	// 是否中赔率奖池奖励
	private int ispool;
	private long time;
	private String fujia;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogtype() {
		return logtype;
	}
	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public String getFujia() {
		return fujia;
	}
	public void setFujia(String fujia) {
		this.fujia = fujia;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public double getBet() {
		return bet;
	}
	public void setBet(double bet) {
		this.bet = bet;
	}
	public double getReward() {
		return reward;
	}
	public void setReward(double reward) {
		this.reward = reward;
	}
	public int getIspool() {
		return ispool;
	}
	public void setIspool(int ispool) {
		this.ispool = ispool;
	}
	public int getRewardtype() {
		return rewardtype;
	}
	public void setRewardtype(int rewardtype) {
		this.rewardtype = rewardtype;
	}
	
	public String getRolenick() {
		return rolenick;
	}
	public void setRolenick(String rolenick) {
		this.rolenick = rolenick;
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
	
	public GameLhjLog(int id, String logtype, String gamename, String roleid, String rolenick, String uuid, double bet,
			double reward, double[] rewardBetween, double todayWin, double totalWin, int rewardtype, int ispool,
			long time, String fujia) {
		super();
		this.id = id;
		this.logtype = logtype;
		this.gamename = gamename;
		this.roleid = roleid;
		this.rolenick = rolenick;
		this.uuid = uuid;
		this.bet = bet;
		this.reward = reward;
		RewardBetween = rewardBetween;
		this.todayWin = todayWin;
		this.totalWin = totalWin;
		this.rewardtype = rewardtype;
		this.ispool = ispool;
		this.time = time;
		this.fujia = fujia;
	}
	public static GameLhjLog build(String gamename, String logtype, String roleid, String rolenick,String uuid, double bet, double reward,int rewardtype, int ispool,String fujia){
		GameLhjLog log = new GameLhjLog();
		double [] between = {0,0};
		Date date = new Date();
		long time = date.getTime();
		
		log.setId(0);
		log.setLogtype(logtype);
		log.setGamename(gamename);
		log.setRoleid(roleid);
		log.setRolenick(rolenick);
		log.setUuid(uuid);
		log.setBet(bet);
		log.setReward(reward);
		log.setRewardtype(rewardtype);
		log.setIspool(ispool);
		log.setTime(time);
		log.setFujia(fujia);
		
		log.setRewardBetween(between);
		log.setTodayWin(0);
		log.setTotalWin(0);
		return log;
	}
	
	public GameLhjLog() {
		super();
	}
	
	@Override
	public String toString() {
		return "GameLhjLog [id=" + id + ", logtype=" + logtype + ", gamename=" + gamename + ", roleid=" + roleid
				+ ", rolenick=" + rolenick + ", uuid=" + uuid + ", bet=" + bet + ", reward=" + reward
				+ ", RewardBetween=" + Arrays.toString(RewardBetween) + ", todayWin=" + todayWin + ", totalWin="
				+ totalWin + ", rewardtype=" + rewardtype + ", ispool=" + ispool + ", time=" + time + ", fujia=" + fujia
				+ "]";
	}
	
	/*public GameLhjLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		GameLhjLog g = new GameLhjLog(
				rs.getInt("id"), 
				rs.getString("logtype"), 
				rs.getString("gamename"), 
				rs.getString("roleid"), 
				rs.getString("rolenick"),
				rs.getString("uuid"), 
				rs.getDouble("bet"),
				rs.getDouble("reward"),
				rs.get
				rs.getString("RewardBetween"),
				rs.getDouble("todayWin"),
				rs.getDouble("totalWin"),
				rs.getInt("rewardtype"),
				rs.getInt("ispool"),
				rs.getLong("time"),
				rs.getString("fujia"));
		return g;
	}*/
	
	
	
}
