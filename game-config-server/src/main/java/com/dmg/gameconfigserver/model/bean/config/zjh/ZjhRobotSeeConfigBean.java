package com.dmg.gameconfigserver.model.bean.config.zjh;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author liubo
 * @Description //TODO 扎金花机器人是否看牌概率配置表
 * @Date 15:26 2019/9/27
 **/
@Data
@TableName("t_dmg_zjh_robot_see_config")
public class ZjhRobotSeeConfigBean extends CommonBean implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 最小轮数
	 */
	private Integer roundMin;
	/**
	 * 最大轮数
	 */
	private Integer roundMax;
	/**
	 * 闷牌概率 列如100%=10000
	 */
	private Integer probabilityStuffy;
	/**
	 * 看牌类型 0:固定概率 1：基础概率计算
	 */
	private Integer seeType;
	/**
	 * 看牌概率 列如100%=10000
	 */
	private Integer probabilitySee;

}
