package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户游戏流水日志
 * 
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("user_game_turnover_log")
public class UserGameTurnoverLogBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 游戏类型
	 */
	private Integer gameType;
	/**
	 * 流水数量
	 */
	private Long turnoverNumber;
	/**
	 * 派彩数量
	 */
	private Long payoutNumber;
	/**
	 * 创建时间
	 */
	private Date createDate;

}
