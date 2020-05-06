package com.dmg.data.common.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 结算接收 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettleRecvDto {
    /** 状态码(0:成功,-1未知错误) */
    private int code;
    /** 当前金币 */
    private BigDecimal curGold;

    /**
     * 设置：当前金币
     *
     * @param curGold 当前金币
     */
    public void setGold(BigDecimal curGold) {
        this.curGold = curGold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
