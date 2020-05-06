package com.dmg.zhajinhuaserver.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-07-02 20:05:07
 */
@Data
@TableName("robot")
public class RobotBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 玩家唯一id
	 */
	private Long userId;
	/**
	 * 头像地址
	 */
	private String headImg;
	/**
	 * 
	 */
	private String nickname;
	/**
	 * 性别
	 */
	private Integer sex;
	/**
	 * 金币
	 */
	private Integer gold;
	/**
	 * 
	 */
	private String province;
	/**
	 * 
	 */
	private String city;

}
