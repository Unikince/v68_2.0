package com.dmg.clubserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 俱乐部成员关系表
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:52
 */
@Data
@TableName("r_club_player")
public class RClubPlayerBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 俱乐部id
	 */
	private Integer clubId;
	/**
	 * 成员id
	 */
	private Integer roleId;
	/**
	 * 成员职位 1:创建者 2:管理员 3:普通成员
	 */
	private Integer position;
	/**
	 * 成员状态(0:解冻 1:冻结)
	 */
	private Integer status;
	/**
	 * 加入时间
	 */
	private Date joinDate;

}
