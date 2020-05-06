package com.dmg.gameconfigserver.model.vo.finance;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 人工充值
 */
@Data
public class PersionRechargeLogCreateRecv {
    /** 订单id */
    private String orderId;
    /** 用户id */
    private Long userId;
    /** 充值类型 */
    private Integer type;
    /** 充值金额 */
    private BigDecimal rechargeAmount = BigDecimal.ZERO;
    /** 充值备注 */
    private String createRemark;
}
