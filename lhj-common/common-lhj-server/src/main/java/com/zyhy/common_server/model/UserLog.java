/**
 * 
 */
package com.zyhy.common_server.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author linanjun
 *
 */
public class UserLog implements RowMapper<UserLog>{

	private int id;
	private String logtype;
	private String gamename;
	private String moid;
	private String userid;
	private long time;
	private String name;
	private double gold;
	private double coin;
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
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getGold() {
		return gold;
	}
	public void setGold(double gold) {
		this.gold = gold;
	}
	public double getCoin() {
		return coin;
	}
	public void setCoin(double coin) {
		this.coin = coin;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public String getMoid() {
		return moid;
	}
	public void setMoid(String moid) {
		this.moid = moid;
	}
	public UserLog() {
		super();
	}
	public UserLog(int id, String logtype, String gamename, String moid, String userid, long time, String name,
			int gold, long coin) {
		super();
		this.id = id;
		this.logtype = logtype;
		this.gamename = gamename;
		this.moid = moid;
		this.userid = userid;
		this.time = time;
		this.name = name;
		this.gold = gold;
		this.coin = coin;
	}
	public UserLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserLog u = new UserLog(rs.getInt("id"), rs.getString("logtype"), rs.getString("gamename"), rs.getString("moid"), rs.getString("userid"), rs.getLong("time"), rs.getString("gamename"), rs.getInt("gold"), rs.getLong("coin"));
		return u;
	}
}
