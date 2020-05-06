package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户优惠日志
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("user_promotion_log")
public class UserPromotionLogBean implements Serializable {
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
	 * 优惠类型
	 */
	private Integer promotionType;
	/**
	 * 优惠金额
	 */
	private Integer promotionNumber;
	/**
	 * 创建时间
	 */
	private Date createDate;

}
