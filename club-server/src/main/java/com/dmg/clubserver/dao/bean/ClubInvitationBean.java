package com.dmg.clubserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 俱乐部邀请表
 * 
 * @author mice
 * @email .com
 * @date 2019-05-30 18:59:30
 */
@Data
@TableName("club_invitation")
public class ClubInvitationBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 邀请人
	 */
	private Integer invitorId;
	/**
	 * 被邀请人
	 */
	private Integer beInvitorId;
	/**
	 * 俱乐部id
	 */
	private Integer clubId;
	/**
	 * 邀请时间
	 */
	private Date inviteDate;
	/**
	 * 是否同意
	 */
	private Integer agree;

}
