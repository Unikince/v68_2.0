package com.dmg.gameconfigserver.model.dto.finance;

import com.dmg.server.common.model.dto.PageConditionDTO;
import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/27 10:18
 * @Version V1.0
 **/
@Data
public class PlatformRechargeDTO extends PageConditionDTO {
    private Long  userId;
    private String orderId;
    private String  thirdOrderId;
    /**
     * 订单状态 1: 等待支付 5:支付失败 10:未到账 15:已完成 20:补单成功
     */
    private Integer orderStatus;
    /**
     * 支付平台id
     */
    private Integer platformId;
}