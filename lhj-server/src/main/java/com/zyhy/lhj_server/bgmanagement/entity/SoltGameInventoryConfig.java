package com.zyhy.lhj_server.bgmanagement.entity;

import lombok.Data;

/**
 * 老虎机游戏库存配置
 * @author Administrator
 *
 */
@Data
public class SoltGameInventoryConfig {
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
