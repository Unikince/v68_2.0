package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统物品配置表
 * 
 * @author mice
 * .com
 * 2019-06-18 17:31:03
 */
@Data
@TableName("sys_item_config")
public class SysItemConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 物品名称
	 */
	private String itemName;
	/**
	 * 物品类型（1.金币；2.积分；3.谢谢惠顾）
	 */
	private Integer itemType;
	/**
	 * 物品描述
	 */
	private String remark;
	/**
	 * 物品图片id(小)
	 */
	private String smallPicId;
	/**
	 * 物品图片id(大)
	 */
	private String bigPicId;

}
