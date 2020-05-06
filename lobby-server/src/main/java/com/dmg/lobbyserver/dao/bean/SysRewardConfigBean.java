package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统奖励配置
 * 
 * @author mice
 * email .com
 * date 2019-06-19 15:40:44
 */
@Data
@TableName("sys_reward_config")
public class SysRewardConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;
	/**
	 * 奖励类型1:新手礼包；2:分享有礼；3.绑定有礼
	 */
	private Integer rewardType;
	/**
	 * 奖励物品id 多个奖励逗号分隔
	 */
	private String rewardDetailId;

}
