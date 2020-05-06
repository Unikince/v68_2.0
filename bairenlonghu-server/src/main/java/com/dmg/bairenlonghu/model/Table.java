package com.dmg.bairenlonghu.model;

import com.dmg.bairenlonghu.common.model.Poker;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 牌桌
 * @Author mice
 * @Date 2019/7/31 10:02
 * @Version V1.0
 **/
@Data
public class Table {
    /**
     *  牌位 1号位为庄家位
     */
    private String tableIndex;
    /**
     *  手牌
     */
    private List<Poker> pokerList = new ArrayList<>();
    /**
     *  牌型 参看枚举:Combination
     */
    private int cardType;
    /**
     *  下注列表
     */
    private List<BigDecimal> betChipList = new ArrayList<>();
    /**
     *  总下注
     */
    private BigDecimal betChipTotal = new BigDecimal(0);
    /**
     *  机器人总下注
     */
    private BigDecimal robotBetChipTotal = new BigDecimal(0);
    /**
     *  玩家总下注
     */
    private BigDecimal playerBetChipTotal = new BigDecimal(0);
}