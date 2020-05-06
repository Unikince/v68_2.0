package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户银行卡
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("bank_card")
public class BankCardBean implements Serializable {
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
	 * 账户姓名
	 */
	private String accountName;
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
	 * 手机号
	 */
	private String phone;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新日期
	 */
	private Date updateDate;

}
