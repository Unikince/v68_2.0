package com.dmg.gameconfigserver.model.bean.config.zjh;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author liubo
 * @Description //TODO 扎金花机器人基础概率配置表
 * @Date 15:26 2019/9/27
 **/
@Data
@TableName("t_dmg_zjh_robot_probability_config")
public class ZjhRobotProbabilityConfigBean extends CommonBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 轮数基础概率 列如100%=10000
	 */
	private Integer probabilityRound;
	/**
	 * 看牌加注人数基础概率 列如100%=10000
	 */
	private Integer probabilitySeeAnnotation;
	/**
	 * 看牌跟注人数基础概率 列如100%=10000
	 */
	private Integer probabilitySeeFollowUp;
	/**
	 * 比牌赢人数基础概率 列如100%=10000
	 */
	private Integer probabilityComparisonWin;

}
