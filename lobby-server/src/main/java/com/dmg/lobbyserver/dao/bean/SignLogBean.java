package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 签到记录表
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("sign_log")
public class SignLogBean implements Serializable {
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
	 * 第几天
	 */
	private Integer signDay;
	/**
	 * 签到时间
	 */
	private Date signDate;

}
