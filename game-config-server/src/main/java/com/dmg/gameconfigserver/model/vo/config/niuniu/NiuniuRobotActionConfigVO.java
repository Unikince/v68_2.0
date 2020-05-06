package com.dmg.gameconfigserver.model.vo.config.niuniu;

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
public class NiuniuRobotActionConfigVO {
    @NotNull(message = "id不能为空", groups = UpdateValid.class)
    private Long id;
    /**
     * 顺序
     */
    @NotNull(message = "sort不能为空", groups = SaveValid.class)
    private Long sort;
    /**
     * 最小牌
     */
    @NotNull(message = "cardMin不能为空", groups = SaveValid.class)
    private Integer cardMin;
    /**
     * 最大牌
     */
    @NotNull(message = "cardMax不能为空", groups = SaveValid.class)
    private Integer cardMax;
    /**
     * 抢类型 枚举
     */
    @NotNull(message = "robType不能为空", groups = SaveValid.class)
    private Integer robType;
    /**
     * 抢概率 列如100%=10000
     */
    @NotNull(message = "probabilityRob不能为空", groups = SaveValid.class)
    private Integer probabilityRob;
    /**
     * 压类型 枚举
     */
    @NotNull(message = "pressureType不能为空", groups = SaveValid.class)
    private Integer pressureType;
    /**
     * 压概率 列如100%=10000
     */
    @NotNull(message = "probabilityPressure不能为空", groups = SaveValid.class)
    private Integer probabilityPressure;

}
