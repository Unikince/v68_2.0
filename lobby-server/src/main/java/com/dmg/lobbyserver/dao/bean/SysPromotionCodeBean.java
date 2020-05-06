package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 优惠代码
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("sys_promotion_code")
public class SysPromotionCodeBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 优惠类型(1.优惠代码2.优惠活动)
	 */
	private int promotionType;
	/**
	 * 优惠码
	 */
	private String promotionCode;
	/**
	 * 存款返利
	 */
	private Integer depositRebate;
	/**
	 * 存款返利类型 1:RMB 2:百分比
	 */
	private Integer depositRebateType;
	/**
	 * 所需流水
	 */
	private Integer turnoverCondition;
	/**
	 * 指定游戏类型
	 */
	private Integer useGameType;
	/**
	 * 优惠上限
	 */
	private Integer promotionLimit;
	/**
	 * 领取条件类型
	 */
	private Integer receiveConditionType;
	/**
	 * 领取条件数量限制
	 */
	private Integer receiveConditionNumber;
	/**
	 * 过期时间
	 */
	private Date expireDate;
	/**
	 * 是否已领取(0:未领取 1:已领取)
	 */
	private Integer hasReceive;

}
