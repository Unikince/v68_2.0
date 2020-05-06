package com.dmg.gameconfigserver.model.bean.config.niuniu;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author liubo
 * @Description //TODO 抢庄牛牛机器人动作配置表
 * @Date 15:26 2019/9/27
 **/
@Data
@TableName("t_dmg_niuniu_robot_action_config")
public class NiuniuRobotActionConfigBean extends CommonBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 最小牌
	 */
	private Integer cardMin;
	/**
	 * 最大牌
	 */
	private Integer cardMax;
	/**
	 * 抢类型 枚举
	 */
	private Integer robType;
	/**
	 * 抢概率 列如100%=10000
	 */
	private Integer probabilityRob;
	/**
	 * 压类型 枚举
	 */
	private Integer pressureType;
	/**
	 * 压概率 列如100%=10000
	 */
	private Integer probabilityPressure;

}
