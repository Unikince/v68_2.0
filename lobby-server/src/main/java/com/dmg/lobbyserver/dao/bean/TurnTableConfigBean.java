package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 大转盘配置
 * 
 * @author mice
 * email .com
 * date 2019-06-19 15:40:44
 */
@Data
@TableName("turn_table_config")
public class TurnTableConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 转盘编号
	 */
	private Integer itemOrder;
	/**
	 * 物品id
	 */
	private Long itemId;
	/**
	 * 物品数量
	 */
	private Integer itemNumber;
	/**
	 * 概率值
	 */
	private Integer probability;

}
