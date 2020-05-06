package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-04-30 14:48:56
 */
@Data
@TableName("return_visit")
public class ReturnVisitBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 回访人id
	 */
	private Long userId;
	/**
	 * 回访电话
	 */
	private String phone;
	/**
	 * 申请时间
	 */
	private Date requestDate;
	/**
	 * 0:未回访 1:已回访
	 */
	private Integer dealStatus;

}
