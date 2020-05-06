package com.zyhy.lhj_server.bgmanagement.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * 老虎机游戏列表
 * @author Administrator
 *
 */
public class SoltGameList implements RowMapper<SoltGameList>{
	// id
	private int id;
	// 名字
	private String name;
	// 代码
	private String code;
	// 状态
	private int status;
	
	
	public SoltGameList() {
		super();
	}
	
	public SoltGameList(int id, String name, String code, int status) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.status = status;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public SoltGameList mapRow(ResultSet rs, int rowNum) throws SQLException {
		SoltGameList sl = new SoltGameList(rs.getInt("id"),rs.getString("name"),rs.getString("code"),rs.getInt("status"));
		return sl;
	}

	@Override
	public String toString() {
		return "SoltList [id=" + id + ", name=" + name + ", code=" + code + ", status=" + status + "]";
	}
	
}
