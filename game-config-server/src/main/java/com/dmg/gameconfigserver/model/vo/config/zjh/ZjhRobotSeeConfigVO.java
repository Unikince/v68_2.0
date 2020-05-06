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
public class ZjhRobotSeeConfigVO {
    @NotNull(message = "id不能为空", groups = UpdateValid.class)
    private Long id;
    /**
     * 顺序
     */
    @NotNull(message = "sort不能为空", groups = SaveValid.class)
    private Long sort;
    /**
     * 最小轮数
     */
    @NotNull(message = "roundMin不能为空", groups = SaveValid.class)
    private Integer roundMin;
    /**
     * 最大轮数
     */
    @NotNull(message = "roundMax不能为空", groups = SaveValid.class)
    private Integer roundMax;
    /**
     * 闷牌概率 列如100%=10000
     */
    @NotNull(message = "probabilityStuffy不能为空", groups = SaveValid.class)
    private Integer probabilityStuffy;
    /**
     * 看牌类型 0:固定概率 1：基础概率计算
     */
    @NotNull(message = "seeType不能为空", groups = SaveValid.class)
    private Integer seeType;
    /**
     * 看牌概率 列如100%=10000
     */
    @NotNull(message = "probabilitySee不能为空", groups = SaveValid.class)
    private Integer probabilitySee;

}
