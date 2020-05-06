package com.dmg.gameconfigserverapi.fish.dto;

import java.io.Serializable;

/**
 * 捕鱼返奖率控制
 */
public class FishCtrlReturnRateDTO implements Serializable {
    /** 串行版本标识 */
    private static final long serialVersionUID = 1L;
    /** 逻辑id */
    private Integer id;
    /** 名称 */
    private String name;
    /** 最低返奖率 */
    private Long minReturnRate;
    /** 最高返奖率 */
    private Long maxReturnRate;
    /** 概率分母 */
    private Long denominator;

    /**
     * 获取：逻辑id
     *
     * @return 逻辑id
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 设置：逻辑id
     *
     * @param id 逻辑id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：名称
     *
     * @return 名称
     */
    public String getName() {
        return this.name;
    }

    /**
     * 设置：名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：最低返奖率
     *
     * @return 最低返奖率
     */
    public Long getMinReturnRate() {
        return this.minReturnRate;
    }

    /**
     * 设置：最低返奖率
     *
     * @param minReturnRate 最低返奖率
     */
    public void setMinReturnRate(Long minReturnRate) {
        this.minReturnRate = minReturnRate;
    }

    /**
     * 获取：最高返奖率
     *
     * @return 最高返奖率
     */
    public Long getMaxReturnRate() {
        return this.maxReturnRate;
    }

    /**
     * 设置：最高返奖率
     *
     * @param maxReturnRate 最高返奖率
     */
    public void setMaxReturnRate(Long maxReturnRate) {
        this.maxReturnRate = maxReturnRate;
    }

    /**
     * 获取：概率分母
     * 
     * @return 概率分母
     */
    public Long getDenominator() {
        return this.denominator;
    }

    /**
     * 设置：概率分母
     * 
     * @param denominator 概率分母
     */
    public void setDenominator(Long denominator) {
        this.denominator = denominator;
    }

    /**
     * 获取：串行版本标识
     *
     * @return 串行版本标识
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
