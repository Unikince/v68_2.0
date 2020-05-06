package com.dmg.bairenzhajinhuaserver.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/31 18:04
 * @Version V1.0
 **/
@Data
public class BetVO {
    /**
     * 是否续压
     */
    private boolean copyBet;
    /**
     * 下注牌桌位
    */
    private String betTableIndex;
    /**
     * 下注金额
     */
    private BigDecimal betChip;
}