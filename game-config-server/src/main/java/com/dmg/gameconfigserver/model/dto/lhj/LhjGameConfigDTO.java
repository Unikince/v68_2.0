package com.dmg.gameconfigserver.model.dto.lhj;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * @author Administrator
 * 老虎机游戏配置
 */
@Data
public class LhjGameConfigDTO {
	/**
	 * 游戏id
	 */
	private int gameId;
	/**
     * 汇率
     */
    private double exchangeRate;
	/**
	 * 场次配置
	 */
	private LhjFieldConfigDTO lhjFieldConfig;
	/**
	 * 库存配置
	 */
	private List<LhjInventoryConfigDTO> lhjInventoryConfig;
	/**
	 * 库存控制配置
	 */
	private LhjInventoryControlDTO  lhjInventoryControl;
	/**
	 * 彩金配置
	 */
	private List<LhjBonusConfigDTO> lhjBonusConfigList;
	/**
	 * 赔率奖池配置
	 */
	private LhjOddsPoolConfigDTO lhjOddsPoolConfig;
	/**
	 * 派奖条件配置
	 */
	private List<LhjPayLimitConfigDTO> lhjPayLimitConfigList;
	/**
	 * 玩家派奖条件配置
	 */
	private List<LhjPlayerPayLimitConfigDTO> LhjPlayerPayLimitConfigList;
}
