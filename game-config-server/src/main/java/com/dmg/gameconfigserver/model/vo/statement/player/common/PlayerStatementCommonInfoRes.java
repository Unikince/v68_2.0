package com.dmg.gameconfigserver.model.vo.statement.player.common;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 玩家报表_信息_公用_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerStatementCommonInfoRes extends PlayerStatementCommonRes {
    /** 玩家ID */
    private long playerId;
    /** 玩家昵称 */
    private String nickname;
    /** 当前余额 */
    private BigDecimal money;
    /*** 总充值 */
    private BigDecimal sumRecharge;
    /*** 总提款 */
    private BigDecimal sumWithdraw;
    /*** 提存差 */
    private BigDecimal diffRechargeSubWithdraw;

    /**
     * 设置：当前余额
     *
     * @param money 当前余额
     */
    public void setMoney(BigDecimal money) {
        this.money = money.setScale(2, BigDecimal.ROUND_HALF_UP);
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

}
