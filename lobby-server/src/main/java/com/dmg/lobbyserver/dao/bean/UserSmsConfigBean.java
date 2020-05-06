package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户短信配置表
 * 
 * @author mice
 * @email .com
 * @date 2019-06-21 17:12:08
 */
@Data
@TableName("user_sms_config")
public class UserSmsConfigBean implements Serializable {
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
	 * 存款到账通知 0:不通知 1:通知
	 */
	private Integer depositToAccount;
	/**
	 * 提款信息通知 0:不通知 1:通知
	 */
	private Integer withdrawalMessage;
	/**
	 * 修改银行卡通知 0:不通知 1:通知
	 */
	private Integer changeBankCard;
	/**
	 * 优惠添加通知 0:不通知 1:通知
	 */
	private Integer offerAdded;
	/**
	 * 修改电话 0:不通知 1:通知
	 */
	private Integer changePhone;
	/**
	 * 修改密码  0:不通知 1:通知
	 */
	private Integer changePassword;

}
