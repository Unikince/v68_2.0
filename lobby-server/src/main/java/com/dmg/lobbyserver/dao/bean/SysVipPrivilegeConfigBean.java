package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * 
 * @author mice
 * @email .com
 * @date 2019-06-20 15:38:34
 */
@Data
@TableName("sys_vip_privilege_config")
public class SysVipPrivilegeConfigBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;
	/**
	 * 会员等级
	 */
	private Integer vipLevel;
	/**
	 * 每日提款次数
	 */
	private Integer dailyWithdrawalTimes;
	/**
	 * 洗码比例
	 */
	private Double washCodeRatio;
	/**
	 * 提款费率
	 */
	private String withdrawalRate;
	/**
	 * 晋级礼金
	 */
	private Integer promotionGift;
	/**
	 * 生日礼金
	 */
	private Integer birthdayGift;
	/**
	 * 游戏类型
	 */
	private  Integer  gameType;
	/**
	 * 升级条件(存款)
	 */
	private  Long  upLevelDepositNum;
	/**
	 * 升级条件(流水)
	 */
	private  Long  upLevelTurnoverNum;
	/**
	 * 保级条件(存款)
	 */
	private  Long  keepLevelDepositNum;
	/**
	 * 保级条件(流水)
	 */
	private  Long  keepLevelTurnoverNum;

}
