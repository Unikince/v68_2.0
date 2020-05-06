package com.dmg.clubserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 举报信息
 * 
 * @author mice
 * @email .com
 * @date 2019-06-04 15:54:21
 */
@Data
@TableName("club_report_info")
public class ClubReportInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 举报玩家id
	 */
	private Integer reportPlayerId;
	/**
	 * 被举报俱乐部id
	 */
	private Integer beReportClubId;
	/**
	 * 举报类型
	 */
	private String reportType;
	/**
	 * 举报时间
	 */
	private Date reportDate;

}
