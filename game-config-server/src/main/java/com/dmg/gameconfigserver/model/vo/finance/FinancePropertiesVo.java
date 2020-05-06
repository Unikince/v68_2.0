package com.dmg.gameconfigserver.model.vo.finance;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 财务配置
 */
@Data
public class FinancePropertiesVo {
    /** 每日提现次数上限 */
    private Integer maxWithdrawalTime;
    /** 每日免费提现次数 */
    private Integer freeWithdrawalTime;
    /** 提现手续费比例 */
    private BigDecimal withdrawalCharge;
    /** 每日系统提现金额上限 */
    private BigDecimal maxSysWithdrawal;
    /** 最低单笔提现金额 */
    private BigDecimal minWithdrawal;
    /** 最高单笔提现金额 */
    private BigDecimal maxWithdrawal;
    /** 玩家提款流水下限 */
    private BigDecimal withdrawalTurnover;
    /** 提款开始时间 */
    private String beginWithdrawal;
    /** 提款截止时间 */
    private String endWithdrawal;
    /** 提款状态 */
    private boolean withdrawalStatus;
    /** 提款说明 */
    private String withdrawalDesc;
}
