package com.zyhy.lhj_server.bgmanagement.feign.dto;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机库存控制
 */
@Data
public class LhjInventoryControlDTO {
	/**
	 * 游戏id
	 */
	private int gameId;
	/**
	 * 游戏名称
	 */
	private String gameName;
	/**
	 * redis名称
	 */
	private String redisName;
	/**
	 * 当前库存值
	 */
	private double currentInventory;
	/**
	 * 设置库存值(正数:系统赢,负数:系统输)
	 */
	private double setInventory;
	
	/**
	 * 当前输赢值
	 */
	private double currentWinLoseValue;
	
	/**
	 * 启用模型
	 */
	private int model;
	/**
	 * 比较类型(1:大于,2:大于等于,3:小于,4:小于等于)
	 */
	private int type;
	/**
	 * 流水值
	 */
	private double waterValue;
}
