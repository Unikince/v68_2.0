package com.dmg.redblackwarserver.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.dmg.redblackwarserver.common.model.BaseSeat;
import com.dmg.redblackwarserver.model.constants.D;

import lombok.Data;

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
    /**
     * 下注 key:牌位 value:下注筹码
     */
    private Map<String, BigDecimal> userBetChipTotal = new HashMap<>();
    /**
     * 赢钱统计 key:牌桌位置 value:赢钱数
     */
    private Map<String, BigDecimal> winGoldMap = new HashMap<>();
    /**
     * 最后一次下注 key:牌位 value:下注筹码
     */
    private Map<String, BigDecimal> lastBetChipInfo = new HashMap<>();

    private boolean canRenew = false;// 能否续投

    private boolean applyBanker = false;// 是否已申请上庄

    private int noBetCount;// 连续未下注局数

    /**
     * 服务费 key:牌位 value:服务费
     */
    private Map<String, BigDecimal> userPumpGoldTotal = new HashMap<>();

    public void clear() {
        this.winGoldMap.clear();
        if (!this.userBetChipTotal.isEmpty()) {
            this.lastBetChipInfo = new HashMap<>(this.userBetChipTotal);
        } else {
            this.lastBetChipInfo = new HashMap<>();
        }
        if (!this.lastBetChipInfo.isEmpty()) {
            this.canRenew = true;
        } else {
            this.canRenew = false;
        }
        this.userBetChipTotal.clear();
        this.userBetChipTotal.put(D.ONE, BigDecimal.ZERO);
        this.userBetChipTotal.put(D.TWO, BigDecimal.ZERO);
        this.userBetChipTotal.put(D.THREE, BigDecimal.ZERO);
        this.userPumpGoldTotal.clear();
        this.setReady(false);
        this.setCurWinGold(new BigDecimal(0));
    }

    @Override
    public int compareTo(Seat o) {
        return this.getPlayer().getGold().compareTo(o.getPlayer().getGold());
    }
}