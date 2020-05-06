package com.dmg.gameconfigserver.model.dto.config;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:43 2019/9/29
 */
@Data
public class GameWaterPoolByWaterDTO {

    /**
     * 水位
     */
    @NotNull(message = "water不能为空")
    private BigDecimal water;
    /**
     * 游戏id
     */
    @NotNull(message = "gameId不能为空")
    private Integer gameId;
    /**
     * 房间等级
     */
    @NotNull(message = "roomLevel不能为空")
    private Integer roomLevel;
}
