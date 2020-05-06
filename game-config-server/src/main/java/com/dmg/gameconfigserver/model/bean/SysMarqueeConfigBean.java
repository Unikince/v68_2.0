package com.dmg.gameconfigserver.model.bean;

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
 * @date 2019-12-25 10:23:23
 */
@Data
@TableName("t_sys_marquee_config")
public class SysMarqueeConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 跑马灯类型 1:普通公告 2:停服公告
	 */
	private Integer marqueeType;
	/**
	 * 需要通知到游戏服
	 */
	private String notifyGameServerIds;
	/**
	 * 展示内容
	 */
	private String notifyContent;
	/**
	 * 开启状态 0:未开启 1:开启
	 */
	private Integer openStatus;
	/**
	 * 开始时间
	 */
	private Date startDate;
	/**
	 * 结束时间
	 */
	private Date endDate;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 创建人id
	 */
	private Long creatorId;
	/**
	 * 更新人id
	 */
	private Long updatorId;

}
