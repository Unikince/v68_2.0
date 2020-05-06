package com.dmg.bairenniuniuserver.model;

import com.dmg.bairenniuniuserver.common.model.BaseSeat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description 座位信息
 * @Author mice
 * @Date 2019/7/29 15:38
 * @Version V1.0
 **/
@Data
public class Seat extends BaseSeat implements Comparable<Seat>, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 是否是庄家
     */
    private boolean banker = false;
    /** 下注
     * key:牌位 value:下注筹码
    */
    private Map<String, BigDecimal> betChipMap = new HashMap<>();
    /**
     *  赢钱统计
     *  key:牌桌位置 value:赢钱数
     */
    private Map<String, BigDecimal> winGoldMap = new HashMap<>();
    /** 上局下注记录
     * key:牌位 value:下注筹码
     */
    private Map<String, List<BigDecimal>> lastBetChipRecordMap = new HashMap<>();
    /** 下注记录
     * key:牌位 value:下注筹码
     */
    private Map<String, List<BigDecimal>> betChipRecordMap = new HashMap<>();












    @Override
    public int compareTo(Seat o) {
        return this.getPlayer().getGold().compareTo(o.getPlayer().getGold());
    }
}