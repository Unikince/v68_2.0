package com.dmg.bairenzhajinhuaserver.model.dto;

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
//    /**
//     * 下注牌桌
//     */
//    private String betTableIndex;
    /**
     * 玩家金币
     */
    private BigDecimal userGold;
    
    /**
     * 下注金额
     */
    private Map<String,BigDecimal> betChipMap = new HashMap<String, BigDecimal>();
    
    /**
     * 牌桌当前下注总额
     */
    private Map<String,BigDecimal> allBetChipTotal = new HashMap<String, BigDecimal>();
    
    /**
     * 玩家当前位置下注总额
     */
    private Map<String,BigDecimal> userBetChipTotal = new HashMap<String, BigDecimal>();
    
    /**
     * 能否续投
     */
    private boolean canRenew;
}