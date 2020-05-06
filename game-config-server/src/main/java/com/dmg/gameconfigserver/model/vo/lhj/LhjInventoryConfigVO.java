package com.dmg.gameconfigserver.model.vo.lhj;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机库存配置
 */
@Data
public class LhjInventoryConfigVO {
	/**
	 * 游戏id
	 */
	@NotNull(message = "gameId不能为空")
	private int gameId;
	/**
	 * 游戏名称
	 */
	@org.hibernate.validator.constraints.NotEmpty(message = "gamename不能为空")
	private String gamename;
	/**
	 * 序号
	 */
	@NotNull(message = "number不能为空")
	private int number;
	/**
	 * 库存最小值
	 */
	@NotNull(message = "svalue不能为空")
	private double svalue;
	/**
	 * 库存最大值
	 */
	@NotNull(message = "bvalue不能为空")
	private double bvalue;
	/**
	 * 赔率
	 */
	@NotNull(message = "odds不能为空")
	private double odds;

}
