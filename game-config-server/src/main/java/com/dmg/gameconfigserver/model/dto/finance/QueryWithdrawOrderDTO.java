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
public class QueryWithdrawOrderDTO extends PageConditionDTO {
    private Long  userId;
    private String orderId;
    /**
     * 订单状态 1: 等待审核 5:审核拒绝 10:未到账 15:已完成 20:补单成功 25:支付中 30:拒绝提现
     */
    private Integer orderStatus;
    /**
     * 审核人员id
     */
    private Long reviewerId;
}