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
public class GameLog implements RowMapper<GameLog> {

	private int id;
	private String logtype;
	private String gamename;
	private String moid;
	private String userid;
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
	public String getMoid() {
		return moid;
	}
	public void setMoid(String moid) {
		this.moid = moid;
	}
	public GameLog(int id, String logtype, String gamename, String moid, String userid, long time, String fujia) {
		super();
		this.id = id;
		this.logtype = logtype;
		this.gamename = gamename;
		this.moid = moid;
		this.userid = userid;
		this.time = time;
		this.fujia = fujia;
	}
	public GameLog() {
		super();
	}
	
	public GameLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		GameLog g = new GameLog(rs.getInt("id"), rs.getString("logtype"), rs.getString("gamename"), rs.getString("moid"), rs.getString("userid"), rs.getLong("time"), rs.getString("fujia"));
		return g;
	}
}
