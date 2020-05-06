package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 大转盘中奖记录
 * 
 * @author mice
 * @email .com
 * @date 2019-06-19 17:25:24
 */
@Data
@TableName("turn_table_win_log")
public class TurnTableWinLogBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 中奖人id
	 */
	private Long winUserId;
	/**
	 * 物品编号
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
	 * 中奖时间
	 */
	private Date winDate;

}
