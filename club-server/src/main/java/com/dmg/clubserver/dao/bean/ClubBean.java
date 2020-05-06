package com.dmg.clubserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 俱乐部
 * 
 * @author mice
 * @email .com
 * @date 2019-05-25 14:18:53
 */
@Data
@TableName("club")
public class ClubBean implements Serializable {
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
	 * 俱乐部名称
	 */
	private String name;
	/**
	 * 等级
	 */
	private Integer level;
	/**
	 * 经验值
	 */
	private Integer experienceNum;
	/**
	 * 当前成员人数
	 */
	private Integer currentMemberNum;
	/**
	 * 成员人数上限
	 */
	private Integer memberNumLimit;
	/**
	 * 房间数上限
	 */
	private Integer roomNumLimit;
	/**
	 * 俱乐部房卡
	 */
	private Integer roomCard;
	/**
	 * 创建人id
	 */
	private Integer creatorId;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 俱乐部广告
	 */
	private String remark;

}
