package com.dmg.gameconfigserver.model.vo.statement.everyday.common;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 每日报表_公用_返回
 */
@Data
public class EveryDayStatementCommonRes {
    /** 总盈利 */
    private BigDecimal sumWin;
    /** 总下注 */
    private BigDecimal sumBet;
    /** 总赔付 */
    private BigDecimal sumPay;
    /** 服务费 */
    private BigDecimal charge;
    /*** 总充值 */
    private BigDecimal sumRecharge;
    /*** 总提款 */
    private BigDecimal sumWithdraw;
    /*** 提存差 */
    private BigDecimal diffRechargeSubWithdraw;
    /*** ARPU */
    private BigDecimal arpu;

    /**
     * 设置：总盈利
     *
     * @param sumWin 总盈利
     */
    public void setSumWin(BigDecimal sumWin) {
        this.sumWin = sumWin.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：总下注
     *
     * @param sumBet 总下注
     */
    public void setSumBet(BigDecimal sumBet) {
        this.sumBet = sumBet.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：总赔付
     *
     * @param sumPay 总赔付
     */
    public void setSumPay(BigDecimal sumPay) {
        this.sumPay = sumPay.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：服务费
     *
     * @param charge 服务费
     */
    public void setCharge(BigDecimal charge) {
        this.charge = charge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：总充值
     *
     * @param sumRecharge 总充值
     */
    public void setSumRecharge(BigDecimal sumRecharge) {
        this.sumRecharge = sumRecharge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：总提款
     *
     * @param sumWithdraw 总提款
     */
    public void setSumWithdraw(BigDecimal sumWithdraw) {
        this.sumWithdraw = sumWithdraw.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：提存差
     *
     * @param diffRechargeSubWithdraw 提存差
     */
    public void setDiffRechargeSubWithdraw(BigDecimal diffRechargeSubWithdraw) {
        this.diffRechargeSubWithdraw = diffRechargeSubWithdraw.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：ARPU
     */
    public void setArpu(BigDecimal arpu) {
        this.arpu = arpu.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}