package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 收货地址
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("receive_address")
public class ReceiveAddressBean implements Serializable {
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
	 * 收获人姓名
	 */
	private String receiverName;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 所在区域
	 */
	private String area;
	/**
	 * 详细地址
	 */
	private String address;

}
