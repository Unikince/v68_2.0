package com.dmg.data.common.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 金币充值提款发送 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldPaySendDto {
    /** 玩家id */
    private long userId;
    /** 充值金币 */
    private BigDecimal payGold;
    /**
     * 充值类型
     * 
     * @see com.dmg.server.common.enums.AccountChangeTypeEnum
     */
    private int type;

    /**
     * 设置：充值提款金币
     *
     * @param payGold 充值提款金币
     */
    public void setPayGold(BigDecimal payGold) {
        this.payGold = payGold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
