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
public class GameLhjLog implements RowMapper<GameLhjLog> {

	private int id;
	private String logtype;
	private String gamename;
	private String roleid;
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
	public GameLhjLog(int id, String logtype, String gamename, String roleid, long time, String fujia) {
		super();
		this.id = id;
		this.logtype = logtype;
		this.gamename = gamename;
		this.roleid = roleid;
		this.time = time;
		this.fujia = fujia;
	}
	
	public static GameLhjLog build(String gamename, String logtype, String roleid, String fujia){
		return new GameLhjLog(0, logtype, gamename, roleid, System.currentTimeMillis(), fujia);
	}
	
	public GameLhjLog() {
		super();
	}
	
	public GameLhjLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		GameLhjLog g = new GameLhjLog(rs.getInt("id"), rs.getString("logtype"), rs.getString("gamename"), rs.getString("roleid"), rs.getLong("time"), rs.getString("fujia"));
		return g;
	}
}
