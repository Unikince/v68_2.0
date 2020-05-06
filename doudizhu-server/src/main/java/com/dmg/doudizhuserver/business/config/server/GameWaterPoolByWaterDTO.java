package com.dmg.doudizhuserver.business.config.server;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GameWaterPoolByWaterDTO {

    private BigDecimal water;

    private Integer gameId;

    private Integer roomLevel;
}
