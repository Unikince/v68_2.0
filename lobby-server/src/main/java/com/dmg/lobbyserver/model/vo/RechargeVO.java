package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description
 * @Author JOCK
 * @Date 2019/7/3 0003
 * @Version V1.0
 **/
@Data
public class RechargeVO {
    @NotBlank
    private Integer money;//充值 金额
    @NotBlank
    private Integer promotionCodeId;//优惠代码ID
    @NotBlank
    private Integer taskId;//优惠活动ID
}
