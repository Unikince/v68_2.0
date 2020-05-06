package com.dmg.bairenniuniuserver.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/30 15:40
 * @Version V1.0
 **/
@Data
public class BetResultNTCDTO {
    /**
     * 是否内场
    */
    private boolean infield;
    /**
     * 玩家座位
     */
    private String seatIndex;
    /**
     * 下注牌桌
     */
    private String betTableIndex;
    /**
     * 下注金额
     */
    private BigDecimal betChip;
    /**
     * 预扣金额
     */
    private BigDecimal preDeductGold;
    /**
     *  总下注
     */
    private BigDecimal betChipTotal = new BigDecimal(0);
    /**
     * 玩家金币
     */
    private BigDecimal userGold;
    /** 下注
     * key:牌位 value:下注筹码
     */
    private Map<String, BigDecimal> betChipMap = new HashMap<>();
}