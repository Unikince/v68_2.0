package com.dmg.gameconfigserver.model.bean.config.bairen;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-09-26 17:09:15
 */
@Data
@TableName("t_bairen_control_config")
public class BairenControlConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 基础配置id
	 */
	private Integer fileBaseConfigId;
	/**
	 * 通杀闪避概率
	 */
	private Double allWinDodgeRate;
	/**
	 * 通赔闪避概率
	 */
	private Double allLoseDodgeRate;
	
	 /**
     * 连开起始概率
     */
    private Double continueOpenStartRate;
    /**
     * 连开递增概率
     */
    private Double continueOpenAddRate;
    /**
     * 连开触发次数下限
     */
    private Integer continueOpenTriggerCount;
	
	/**
	 * 最大赔付
	 */
	private Integer maxPayout;
	/**
 	* 最大赔付参考值
 	*/
	private Integer maxPayoutReferenceValue;
	/**
	 * 牌型赔率
	 */
	private String cardTypeMultiple;

}
