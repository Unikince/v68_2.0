package com.dmg.lobbyserver.controller.pay.jqpay;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2020/1/10 11:20
 * @Version V1.0
 **/
@Data
public class WithdrawCreateOrderVO {
    // 提现用户id
    private Long userId;
    // 提现账号类型
    private Integer payType;
    // 提现账号
    private String payAccount;
    // 提现金额
    private BigDecimal payAmount;
    // 提现密码
    private String password;

}