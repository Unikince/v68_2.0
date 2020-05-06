package com.dmg.data.common.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 结算发送 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettleSendDto {
    /** 游戏id */
    private int gameId;
    /** 玩家id */
    private long userId;
    /** 改变的金币,任意金额 */
    private BigDecimal changeGold;

    /**
     * 设置：改变的金币
     *
     * @param changeGold 改变的金币
     */
    public void setChangeGold(BigDecimal changeGold) {
        this.changeGold = changeGold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
