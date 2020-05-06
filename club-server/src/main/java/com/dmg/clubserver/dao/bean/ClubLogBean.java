package com.dmg.clubserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 俱乐部记录
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:53
 */
@Data
@TableName("club_log")
public class ClubLogBean implements Serializable {
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
	 * 操作人id
	 */
	private Integer operatorId;
	/**
	 * 操作人职位
	 */
	private Integer position;
	/**
	 * 被操作人id
	 */
	private Integer beOperatorId;
	/**
	 * 操作类型
	 */
	private Integer operateType;
	/**
	 * 操作日期
	 */
	private Date operateDate;
	/**
	 * 描述
	 */
	private String remark;

}
