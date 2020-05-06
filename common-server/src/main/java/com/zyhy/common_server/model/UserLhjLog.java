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
public class UserLhjLog implements RowMapper<UserLhjLog>{

	private int id;
	private String logtype;
	private String gamename;
	private String roleid;
	private long time;
	private String name;
	private double gold;
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
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public UserLhjLog() {
		super();
	}
	public UserLhjLog(int id, String logtype, String gamename, String roleid, long time, String name,
			double gold) {
		super();
		this.id = id;
		this.logtype = logtype;
		this.gamename = gamename;
		this.roleid = roleid;
		this.time = time;
		this.name = name;
		this.gold = gold;
	}
	
	public static UserLhjLog build(String gamename, String logtype, String roleid, String name,
			double gold){
		return new UserLhjLog(0, logtype, gamename, roleid, System.currentTimeMillis(), name, gold);
	}
	
	public UserLhjLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserLhjLog u = new UserLhjLog(rs.getInt("id"), rs.getString("logtype"), rs.getString("gamename"), rs.getString("roleid"), rs.getLong("time"), rs.getString("gamename"), rs.getInt("gold"));
		return u;
	}
}
