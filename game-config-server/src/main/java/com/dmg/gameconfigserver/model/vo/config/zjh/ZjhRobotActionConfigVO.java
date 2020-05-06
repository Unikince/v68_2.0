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
public class ZjhRobotActionConfigVO  {
	@NotNull(message = "id不能为空", groups = UpdateValid.class)
	private Long id;
	/**
	 * 顺序
	 */
	@NotNull(message = "sort不能为空", groups = SaveValid.class)
	private Long sort;
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
	@NotNull(message = "isSee不能为空", groups = SaveValid.class)
	private Boolean isSee;
	/**
	 * 跟注类型 0:固定概率 1：基础概率计算
	 */
	@NotNull(message = "followUpType不能为空", groups = SaveValid.class)
	private Integer followUpType;
	/**
	 * 跟注概率 列如100%=10000
	 */
	@NotNull(message = "probabilityFollowUp不能为空", groups = SaveValid.class)
	private Integer probabilityFollowUp;
	/**
	 * 加注类型 0:固定概率 1：基础概率计算
	 */
	@NotNull(message = "annotationType不能为空", groups = SaveValid.class)
	private Integer annotationType;
	/**
	 * 加注概率 列如100%=10000
	 */
	@NotNull(message = "probabilityAnnotation不能为空", groups = SaveValid.class)
	private Integer probabilityAnnotation;
	/**
	 * 比牌类型 0:固定概率 1：基础概率计算
	 */
	@NotNull(message = "comparisonType不能为空", groups = SaveValid.class)
	private Integer comparisonType;
	/**
	 * 比牌概率 列如100%=10000
	 */
	@NotNull(message = "probabilityComparison不能为空", groups = SaveValid.class)
	private Integer probabilityComparison;
	/**
	 * 弃牌类型 0:固定概率 1：基础概率计算
	 */
	@NotNull(message = "discardType不能为空", groups = SaveValid.class)
	private Integer discardType;
	/**
	 * 弃牌概率 列如100%=10000
	 */
	@NotNull(message = "probabilityDiscard不能为空", groups = SaveValid.class)
	private Integer probabilityDiscard;

}
