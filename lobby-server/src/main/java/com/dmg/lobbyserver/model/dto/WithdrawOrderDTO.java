package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2020/1/10 15:52
 * @Version V1.0
 **/
@Data
public class WithdrawOrderDTO {
    // 序号
    private Integer rank;
    // 提现金额
    private BigDecimal withdrawAmount;
    // 服务费
    private BigDecimal serviceCharges;
    // 到账金额
    private BigDecimal account;
    // 提现类型
    private Integer bankType;
    // 申请提现时间
    private Date applyDate;
    //  订单状态
    private Integer orderStatus;
}