package com.dmg.gameconfigserver.model.vo.config.zjh;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:26 2019/9/27
 **/
@Data
public class ZjhRobotProbabilityConfigVO{
	@NotNull(message = "id不能为空", groups = UpdateValid.class)
	private Long id;
	/**
	 * 顺序
	 */
	@NotNull(message = "sort不能为空", groups = SaveValid.class)
	private Long sort;
	/**
	 * 轮数基础概率 列如100%=10000
	 */
	@NotNull(message = "probabilityRound不能为空", groups = SaveValid.class)
	private Integer probabilityRound;
	/**
	 * 看牌加注人数基础概率 列如100%=10000
	 */
	@NotNull(message = "probabilitySeeAnnotation不能为空", groups = SaveValid.class)
	private Integer probabilitySeeAnnotation;
	/**
	 * 看牌跟注人数基础概率 列如100%=10000
	 */
	@NotNull(message = "probabilitySeeFollowUp不能为空", groups = SaveValid.class)
	private Integer probabilitySeeFollowUp;
	/**
	 * 比牌赢人数基础概率 列如100%=10000
	 */
	@NotNull(message = "probabilityComparisonWin不能为空", groups = SaveValid.class)
	private Integer probabilityComparisonWin;

}
