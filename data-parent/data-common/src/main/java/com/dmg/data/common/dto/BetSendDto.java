package com.dmg.data.common.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 下注发送 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetSendDto {
    /** 游戏id */
    private int gameId;
    /** 玩家id */
    private long userId;
    /** 减少的金币，负数或0 */
    private BigDecimal decGold;

    /**
     * 设置：
     *
     * @param decGold 减少的金币
     */
    public void decGold(BigDecimal decGold) {
        this.decGold = decGold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
