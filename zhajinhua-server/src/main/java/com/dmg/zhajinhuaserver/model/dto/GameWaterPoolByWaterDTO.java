package com.dmg.zhajinhuaserver.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:43 2019/9/29
 */
@Data
public class GameWaterPoolByWaterDTO {

    private BigDecimal water;

    private Integer gameId;

    private Integer roomLevel;
}
