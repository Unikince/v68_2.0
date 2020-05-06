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
public class IngotLog implements RowMapper<IngotLog> {

	private int id;
	private String type;
	private String logtype;
	private String gamename;
	private String moid;
	private String userid;
	private long time;
	private double oldvalue;
	private double updatevalue;
	private double coinvalue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public double getOldvalue() {
		return oldvalue;
	}

	public void setOldvalue(double oldvalue) {
		this.oldvalue = oldvalue;
	}

	public double getUpdatevalue() {
		return updatevalue;
	}

	public void setUpdatevalue(double updatevalue) {
		this.updatevalue = updatevalue;
	}

	public double getCoinvalue() {
		return coinvalue;
	}

	public void setCoinvalue(double coinvalue) {
		this.coinvalue = coinvalue;
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

	public IngotLog() {
		super();
	}


	public IngotLog(int id, String type, String logtype, String gamename, String moid, String userid, long time,
			long oldvalue, long updatevalue, long coinvalue) {
		super();
		this.id = id;
		this.type = type;
		this.logtype = logtype;
		this.gamename = gamename;
		this.moid = moid;
		this.userid = userid;
		this.time = time;
		this.oldvalue = oldvalue;
		this.updatevalue = updatevalue;
		this.coinvalue = coinvalue;
	}

	public IngotLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		IngotLog i = new IngotLog();
		i.setId(rs.getInt("id"));
		i.setType(rs.getString("type"));
		i.setLogtype(rs.getString("logtype"));
		i.setGamename(rs.getString("gamename"));
		i.setMoid(rs.getString("moid"));
		i.setUserid(rs.getString("userid"));
		i.setTime(rs.getLong("time"));
		i.setOldvalue(rs.getLong("oldvalue"));
		i.setUpdatevalue(rs.getLong("updatevalue"));
		i.setCoinvalue(rs.getLong("coinvalue"));
		return i;
	}
}
