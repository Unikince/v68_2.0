package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 签到配置表
 * 
 * @author mice
 * @email .com
 * @date 2019-06-19 15:40:44
 */
@Data
@TableName("sign_config")
public class SignConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 第几天
	 */
	private Integer signDay;
	/**
	 * 物品id
	 */
	private Long itemId;
	/**
	 * 物品数量
	 */
	private Integer itemNumber;

}
