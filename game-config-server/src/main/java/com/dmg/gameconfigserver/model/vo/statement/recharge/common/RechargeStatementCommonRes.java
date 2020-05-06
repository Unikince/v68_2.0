package com.dmg.gameconfigserver.model.vo.statement.recharge.common;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 充值报表_公用_返回
 */
@Data
public class RechargeStatementCommonRes {
    /** 充值类型 */
    private String type;
    /** 充值总额 */
    private BigDecimal sumRecharge;
    /** 充值总人数 */
    private int sumPersion;
    /** 充值总次数 */
    private int sumTimes;
    /** 单笔最大 */
    private BigDecimal maxRecharge;
    /** 单笔最小 */
    private BigDecimal minRecharge;
    /** 平均人充值 */
    private BigDecimal avePersionRecharge;
    /** 平均次充值 */
    private BigDecimal avetTimesRecharge;
    /** 平均人充次 */
    private BigDecimal avetPersionTimes;

    /**
     * 设置：充值总额
     *
     * @param sumRecharge 充值总额
     */
    public void setSumRecharge(BigDecimal sumRecharge) {
        this.sumRecharge = sumRecharge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：单笔最大
     *
     * @param maxRecharge 单笔最大
     */
    public void setMaxRecharge(BigDecimal maxRecharge) {
        this.maxRecharge = maxRecharge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：单笔最小
     *
     * @param minRecharge 单笔最小
     */
    public void setMinRecharge(BigDecimal minRecharge) {
        this.minRecharge = minRecharge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：平均人充值
     *
     * @param avePersionRecharge 平均人充值
     */
    public void setAvePersionRecharge(BigDecimal avePersionRecharge) {
        this.avePersionRecharge = avePersionRecharge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：平均次充值
     *
     * @param avetTimesRecharge 平均次充值
     */
    public void setAvetTimesRecharge(BigDecimal avetTimesRecharge) {
        this.avetTimesRecharge = avetTimesRecharge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：平均人充次
     *
     * @param avetPersionTimes 平均人充次
     */
    public void setAvetPersionTimes(BigDecimal avetPersionTimes) {
        this.avetPersionTimes = avetPersionTimes.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}