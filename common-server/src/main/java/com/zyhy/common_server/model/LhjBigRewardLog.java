/**
 * 
 */
package com.zyhy.common_server.model;

import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author ASUS
 *
 */
public class LhjBigRewardLog {

	public static final int TYPE_NORMAL = 1;
	
	public static final int TYPE_FREE = 2;
	
	private int id;
	/**游戏名*/
	private String gamename;
	/**玩家id*/
	private String roleid;
	/**线数*/
	private int line;
	/**单线下注额度*/
	private double bet;
	/**总奖励*/
	private double reward;
	/**游戏类型 1普通下注 2免费下注*/
	private int type;
	/**游戏开始时间*/
	private long time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime() {
		return DateFormatUtils.format(new Date(time), "yyyy-MM-dd HH:mm:ss");
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	@Override
	public String toString() {
		return "LhjBigRewardLog [id=" + id + ", gamename=" + gamename
				+ ", roleid=" + roleid + ", line=" + line + ", bet=" + bet
				+ ", reward=" + reward + ", type=" + type + ", time=" + time
				+ "]";
	}
	
}
