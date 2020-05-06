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
public class QueryAbnormalOrderDTO extends PageConditionDTO {
    //private Integer  orderType;//1:充值订单 2:提现订单
    private String orderId;
}