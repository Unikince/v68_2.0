package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("email")
public class EmailBean implements Serializable {
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
	 * 邮件类型(1:站内信息 2:优惠代码)
	 */
	private Integer emailType;
	/**
	 * 邮件名称
	 */
	private String emailName;
	/**
	 * 邮件内容信息
	 */
	private String emailContent;
	/**
	 * 优惠代码
	 */
	private String promotionCode;
	/**
	 * 是否已领取(0:未领取 1:已领取)
	 */
	private Integer receive;
	/**
	 * 是否阅读(0:未阅读 1:已阅读)
	 */
	private Integer hasRead;
	/**
	 * 过期时间
	 */
	private Date expireDate;
	/**
	 * 发送时间
	 */
	private Date sendDate;

}
