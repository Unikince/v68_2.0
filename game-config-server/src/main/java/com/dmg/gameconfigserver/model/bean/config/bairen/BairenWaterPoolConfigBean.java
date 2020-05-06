package com.dmg.gameconfigserver.model.bean.config.bairen;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 *
 * @author mice
 * @email .com
 * @date 2019-09-26 17:09:15
 */
@Data
@TableName("t_bairen_water_pool_config")
public class BairenWaterPoolConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 场次基础配置id
	 */
	private Integer fileBaseConfigId;
	/**
	 * 顺序
	 */
	private Integer waterOrder;
	/**
	 * 低水位线
	 */
	private Long waterLow;
	/**
	 * 高
	 */
	private Long waterHigh;
	/**
	 * 胜率
	 */
	private Double controlExecuteRate;

}
