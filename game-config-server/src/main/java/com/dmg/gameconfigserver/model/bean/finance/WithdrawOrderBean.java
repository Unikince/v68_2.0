package com.dmg.gameconfigserver.model.bean.finance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-12-30 14:07:58
 */
@Data
@TableName("t_withdraw_order")
public class WithdrawOrderBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 订单id
	 */
	private String orderId;
	/**
	 * 第三方订单id
	 */
	private String thirdOrderId;
	/**
	 * 平台id
	 */
	private Integer platformId;

	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 提现金额
	 */
	private BigDecimal withdrawAmount;
	/**
	 * 补发金额
	 */
	private BigDecimal replenishAccount;
	/**
	 * 服务费
	 */
	private BigDecimal serviceCharges;
	/**
	 * 到账金额
	 */
	private BigDecimal account;
	/**
	 * 订单状态 1: 等待审核 5:审核拒绝 10:未到账 15:已完成 20:补单成功 25:支付中 30:拒绝提现
	 */
	private Integer orderStatus;
	/**
	 * 申请时间
	 */
	private Date applyDate;
	/**
	 * 审核时间
	 */
	private Date reviewDate;
	/**
	 * 到账时间
	 */
	private Date accountDate;
	/**
	 * 审核人id
	 */
	private Long reviewerId;
	/**
	 * 审核人名字
	 */
	private String reviewerName;
	/**
	 * 审核备注
	 */
	private String reviewRemark;
	/**
	 * 拨款备注
	 */
	private String appropriateRemark;
	/**
	 * 拨款人id
	 */
	private Long appropriatorId;
	/**
	 * 拨款人名字
	 */
	private String appropriatorName;
	/**
	 * 账户姓名
	 */
	private String bankAccountName;
	/**
	 * 银行类型
	 */
	private Integer bankType;
	/**
	 * 银行卡号
	 */
	private String bankCardNum;
	/**
	 * 开户省份
	 */
	private String openAccountProvince;
	/**
	 * 开户城市
	 */
	private String openAccountCity;
	/**
	 * 开户支行
	 */
	private String openAccountBranchBank;
	/**
	 * 锁定人员id
	 */
	private Long lockUserId;

}
