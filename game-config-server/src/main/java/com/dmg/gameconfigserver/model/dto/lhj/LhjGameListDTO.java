package com.dmg.gameconfigserver.model.dto.lhj;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import lombok.Data;

/**
 * 老虎机游戏列表
 * @author Administrator
 *
 */
@Data
public class LhjGameListDTO {
	/**
	 * 游戏id
	 */
	private int id;
	/**
	 * 游戏名字
	 */
	private String name;
	/**
	 * 游戏代码
	 */
	private String code;
	/**
	 * 状态
	 */
	private int status;
	
}
