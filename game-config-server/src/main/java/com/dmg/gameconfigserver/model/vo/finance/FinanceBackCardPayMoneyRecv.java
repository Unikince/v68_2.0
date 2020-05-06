package com.dmg.gameconfigserver.model.vo.finance;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 财务银行卡--充值
 */
@Data
public class FinanceBackCardPayMoneyRecv {
    /** ID */
    private Long id;
    /** 金额 */
    private BigDecimal money;
}
