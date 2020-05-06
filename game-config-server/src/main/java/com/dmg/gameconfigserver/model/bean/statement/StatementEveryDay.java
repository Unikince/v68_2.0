package com.dmg.gameconfigserver.model.bean.statement;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 每日报表
 */
@Data
@TableName("statement_everyday")
public class StatementEveryDay {
    /** 逻辑键 */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 时间 */
    private java.sql.Date dayStr;
    /** 新增人数 */
    private int newPlayerNum = 0;
    /** 活跃人数 */
    private int activePlayerNum = 0;
    /** 总盈利 */
    private BigDecimal sumWin = BigDecimal.ZERO;
    /** 总下注 */
    private BigDecimal sumBet = BigDecimal.ZERO;
    /** 总赔付 */
    private BigDecimal sumPay = BigDecimal.ZERO;
    /** 服务费 */
    private BigDecimal charge = BigDecimal.ZERO;
    /*** 总充值 */
    private BigDecimal sumRecharge = BigDecimal.ZERO;
    /*** 总提款 */
    private BigDecimal sumWithdraw = BigDecimal.ZERO;
    /*** 提存差 */
    private BigDecimal diffRechargeSubWithdraw = BigDecimal.ZERO;
    /*** ARPU(充值总额/注册人数) */
    private BigDecimal arpu = BigDecimal.ZERO;

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
     *
     * @param aRPU ARPU
     */
    public void setArpu(BigDecimal arpu) {
        this.arpu = arpu.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}