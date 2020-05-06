package com.dmg.clubserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 俱乐部踢人记录
 * 
 * @author mice
 * @email .com
 * @date 2019-05-28 17:23:48
 */
@Data
@TableName("club_kick_out_log")
public class ClubKickOutLogBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
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
	 * 操作人id
	 */
	private Integer operatorId;
	/**
	 * 操作时间
	 */
	private Date operateDate;

}
