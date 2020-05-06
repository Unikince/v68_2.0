package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户提款日志
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("user_withdrawal_log")
public class UserWithdrawalLogBean implements Serializable {
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
	 * 订单编号
	 */
	private Integer orderCode;
	/**
	 * 提款金额
	 */
	private Integer withdrawalNumber;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 订单状态(1:审核中 2: 成功)
	 */
	private Integer orderStatus;

}
