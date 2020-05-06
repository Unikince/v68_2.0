package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统奖励配置
 * 
 * @author mice
 * @email .com
 * @date 2019-06-19 15:40:43
 */
@Data
@TableName("sys_reward_config_detail")
public class SysRewardConfigDetailBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 物品id
	 */
	private Long itemId;
	/**
	 * 物品数量
	 */
	private Integer itemNum;

}
