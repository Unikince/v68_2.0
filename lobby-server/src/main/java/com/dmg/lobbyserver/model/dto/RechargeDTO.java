package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description
 * @Author JOCK
 * @Date 2019/7/3 0003
 * @Version V1.0
 **/
@Data
public class RechargeDTO {
    @NotBlank
    private String orderNo;//订单编号
    @NotBlank
    private Integer money;//充值金额
}
