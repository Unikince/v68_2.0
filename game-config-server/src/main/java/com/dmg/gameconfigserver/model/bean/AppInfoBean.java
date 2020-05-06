package com.dmg.gameconfigserver.model.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-11-14 14:07:20
 */
@Data
@TableName("t_app_info")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppInfoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * app名称
	 */
	private String appName;
	/**
	 * app版本
	 */
	private String appVersion;
	/**
	 * 创建人id
	 */
	private Long creatorId;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新人id
	 */
	private Long updatorId;
	/**
	 * 更新时间
	 */
	private Date updateDate;

}
