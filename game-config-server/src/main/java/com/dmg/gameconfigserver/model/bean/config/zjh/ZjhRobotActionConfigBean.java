package com.dmg.gameconfigserver.model.bean.config.zjh;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author liubo
 * @Description //TODO 扎金花机器人动作概率配置表
 * @Date 15:26 2019/9/27
 **/
@Data
@TableName("t_dmg_zjh_robot_action_config")
public class ZjhRobotActionConfigBean extends CommonBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 牌型
	 */
	private Integer cardType;
	/**
	 * 最小牌
	 */
	private Integer cardMin;
	/**
	 * 最大牌
	 */
	private Integer cardMax;
	/**
	 * 是否看牌
	 */
	private Boolean isSee;
	/**
	 * 跟注类型 0:固定概率 1：基础概率计算
	 */
	private Integer followUpType;
	/**
	 * 跟注概率 列如100%=10000
	 */
	private Integer probabilityFollowUp;
	/**
	 * 加注类型 0:固定概率 1：基础概率计算
	 */
	private Integer annotationType;

	/**
	 * 加注概率 列如100%=10000
	 */
	private Integer probabilityAnnotation;

	/**
	 * 比牌类型 0:固定概率 1：基础概率计算
	 */
	private Integer comparisonType;

	/**
	 * 比牌概率 列如100%=10000
	 */
	private Integer probabilityComparison;

	/**
	 * 弃牌类型 0:固定概率 1：基础概率计算
	 */
	private Integer discardType;
	/**
	 * 弃牌概率 列如100%=10000
	 */
	private Integer probabilityDiscard;

}
