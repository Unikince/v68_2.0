package com.dmg.gameconfigserver.model.bean.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @Author liubo
 * @Description //TODO 游戏水池配置表
 * @Date 15:26 2019/9/27
 **/
@Data
@TableName("t_dmg_game_water_pool_config")
public class GameWaterPoolConfigBean extends CommonBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 游戏类型
	 */
	private Integer gameId;
	/**
	 * 低水位线
	 */
	private BigDecimal waterLow;
	/**
	 * 高
	 */
	private BigDecimal waterHigh;
	/**
	 * 房间等级
	 */
	private Integer roomLevel;
	/**
	 * 基础概率 列如100%=10000
	 */
	private Integer probabilityBasics;
	/**
	 * 增长概率 列如100%=10000
	 */
	private Integer probabilityIncrease;
	/**
	 * 是否系统
	 */
	private Boolean isSystem;

}
