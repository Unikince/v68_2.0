package com.zyhy.lhj_server.bgmanagement.feign.dto;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机库存配置
 */
@Data
public class LhjInventoryConfigDTO {
	/**
	 * 游戏id
	 */
	private int gameId;
	/**
	 * 游戏名称
	 */
	private String gamename;
	/**
	 * 序号
	 */
	private int number;
	/**
	 * 库存最小值
	 */
	private double svalue;
	/**
	 * 库存最大值
	 */
	private double bvalue;
	/**
	 * 赔率
	 */
	private double odds;

}
