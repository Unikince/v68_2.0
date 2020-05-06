package com.dmg.clubserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 俱乐部申请表
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:53
 */
@Data
@TableName("club_join_request")
public class ClubJoinRequestBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 申请人id
	 */
	private Integer requestorId;
	/**
	 * 俱乐部id
	 */
	private Integer clubId;
	/**
	 * 申请时间
	 */
	private Date requestDate;
	/**
	 * 审核人id
	 */
	private Integer reviewerId;
	/**
	 * 拒绝时间
	 */
	private Date reviewDate;

}
