/**
 * 
 */
package com.zyhy.common_server.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @author lnj 商户平台日志
 */
public class BusinessLog implements RowMapper<BusinessLog> {

	// 编号
	private int id;
	// 商户Id
	private String cpid;
	// 操作类型
	private String logtype;
	// 权限Id
	private String roletype;
	// 操作Ip
	private String ipinfos;
	// 创建时间
	private long time;

	public BusinessLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		BusinessLog info = new BusinessLog();
		info.setId(rs.getInt("id"));
		info.setCpid(rs.getString("cpid"));
		info.setRoletype(rs.getString("roletype"));
		info.setLogtype(rs.getString("logtype"));
		info.setIpinfos(rs.getString("ipinfos"));
		info.setTime(rs.getLong("time"));
		return info;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCpid() {
		return cpid;
	}

	public void setCpid(String cpid) {
		this.cpid = cpid;
	}

	public String getRoletype() {
		return roletype;
	}

	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}

	public String getLogtype() {
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getIpinfos() {
		return ipinfos;
	}

	public void setIpinfos(String ipinfos) {
		this.ipinfos = ipinfos;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
