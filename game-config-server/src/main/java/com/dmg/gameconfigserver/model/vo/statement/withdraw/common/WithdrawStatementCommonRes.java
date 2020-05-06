package com.dmg.gameconfigserver.model.vo.statement.withdraw.common;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 提款报表_公用_返回
 */
@Data
public class WithdrawStatementCommonRes {
    /** 提款总额 */
    private BigDecimal sumWithdraw;
    /** 提款总人数 */
    private int sumPersion;
    /** 提款总次数 */
    private int sumTimes;
    /** 单笔最大 */
    private BigDecimal maxWithdraw;
    /** 单笔最小 */
    private BigDecimal minWithdraw;
    /** 平均人提款 */
    private BigDecimal avePersionWithdraw;
    /** 平均次提款 */
    private BigDecimal avetTimesWithdraw;
    /** 平均人提次 */
    private BigDecimal avetPersionTimes;

    /**
     * 设置：提款总额
     * 
     * @param sumWithdraw 提款总额
     */
    public void setSumWithdraw(BigDecimal sumWithdraw) {
        this.sumWithdraw = sumWithdraw.setScale(2, BigDecimal.ROUND_HALF_UP);;
    }

    /**
     * 设置：单笔最大
     * 
     * @param maxWithdraw 单笔最大
     */
    public void setMaxWithdraw(BigDecimal maxWithdraw) {
        this.maxWithdraw = maxWithdraw.setScale(2, BigDecimal.ROUND_HALF_UP);;
    }

    /**
     * 设置：单笔最小
     * 
     * @param minWithdraw 单笔最小
     */
    public void setMinWithdraw(BigDecimal minWithdraw) {
        this.minWithdraw = minWithdraw.setScale(2, BigDecimal.ROUND_HALF_UP);;
    }

    /**
     * 设置：平均人提款
     * 
     * @param avePersionWithdraw 平均人提款
     */
    public void setAvePersionWithdraw(BigDecimal avePersionWithdraw) {
        this.avePersionWithdraw = avePersionWithdraw.setScale(2, BigDecimal.ROUND_HALF_UP);;
    }

    /**
     * 设置：平均次提款
     * 
     * @param avetTimesWithdraw 平均次提款
     */
    public void setAvetTimesWithdraw(BigDecimal avetTimesWithdraw) {
        this.avetTimesWithdraw = avetTimesWithdraw.setScale(2, BigDecimal.ROUND_HALF_UP);;
    }

    /**
     * 设置：平均人提次
     * 
     * @param avetPersionTimes 平均人提次
     */
    public void setAvetPersionTimes(BigDecimal avetPersionTimes) {
        this.avetPersionTimes = avetPersionTimes.setScale(2, BigDecimal.ROUND_HALF_UP);;
    }
}