package com.dmg.gameconfigserver.model.vo.config;

import com.dmg.gameconfigserver.model.vo.group.SaveValid;
import com.dmg.gameconfigserver.model.vo.group.UpdateValid;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:33 2019/9/27
 */
@Data
public class GameWaterPoolConfigVO {
    @NotNull(message = "id不能为空", groups = UpdateValid.class)
    private Long id;
    /**
     * 顺序
     */
    @NotNull(message = "sort不能为空", groups = SaveValid.class)
    private Long sort;
    /**
     * 游戏类型
     */
    @NotNull(message = "gameId不能为空", groups = SaveValid.class)
    private Integer gameId;
    /**
     * 低水位线
     */
    private BigDecimal waterLow;
    /**
     * 高
     */
    private BigDecimal waterHigh;
    /**
     * 房间等级
     */
    @NotNull(message = "roomLevel不能为空", groups = SaveValid.class)
    private Integer roomLevel;
    /**
     * 基础概率 列如100%=10000
     */
    @NotNull(message = "probabilityBasics不能为空", groups = SaveValid.class)
    private Integer probabilityBasics;
    /**
     * 增长概率 列如100%=10000
     */
    @NotNull(message = "probabilityIncrease不能为空", groups = SaveValid.class)
    private Integer probabilityIncrease;
    /**
     * 是否系统
     */
    @NotNull(message = "isSystem不能为空", groups = SaveValid.class)
    private Boolean isSystem;
}


