package com.dmg.doudizhuserver.business.config.local;

import java.math.BigDecimal;

import lombok.Data;

/** 房间配置 */
@Data
public class RoomConfig {
    /** 房间等级 */
    public int level;
    /** 房间名称 */
    public String name;
    /** 是否开启 */
    private boolean open;
    /** 人数上限 */
    private Integer playerUpLimit;
    /** 服务费百分比 */
    private BigDecimal pumpRate;
    /** 底分 */
    private BigDecimal baseScore;
    /** 准入分数 */
    private BigDecimal enterScore;
    /** 最大倍数 */
    private int maxMultiple;

    /**
     * 设置：服务费百分比
     *
     * @param pumpRate 服务费百分比
     */
    public void setPumpRate(BigDecimal pumpRate) {
        this.pumpRate = pumpRate.setScale(6, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：底分
     *
     * @param baseScore 底分
     */
    public void setBaseScore(BigDecimal baseScore) {
        this.baseScore = baseScore.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：准入金额
     *
     * @param enterScore 准入金额
     */
    public void setEnterScore(BigDecimal enterScore) {
        this.enterScore = enterScore.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
