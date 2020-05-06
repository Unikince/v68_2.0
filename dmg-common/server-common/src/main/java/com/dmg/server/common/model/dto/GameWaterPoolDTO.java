package com.dmg.server.common.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:46 2019/9/29
 */
@Data
public class GameWaterPoolDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 游戏类型
     */
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
    private Integer roomLevel;
    /**
     * 基础概率 列如100%=10000
     */
    private Integer probabilityBasics;
    /**
     * 增长概率 列如100%=10000
     */
    private Integer probabilityIncrease;
    /**
     * 是否系统
     */
    private Boolean isSystem;
}
