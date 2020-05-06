package com.dmg.doudizhuserver.business.model;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 玩家结算信息
 */
@Data
public class PlayerSettle {
    /** 玩家id */
    private long playerId;
    /** 昵称 */
    private String nickName;
    /** 是否是地主 */
    private boolean landlord;
    /** 游戏前金币 */
    private BigDecimal beforeGold;
    /** 游戏后金币 */
    private BigDecimal afterGold;
    /** 输赢金币(未扣服务费) */
    private BigDecimal gold;
    /** 输赢金币(已扣服务费) */
    private BigDecimal winGold;
    /** 玩家剩余牌 */
    private List<Integer> cards;
    /** 服务费 */
    @JSONField(serialize = false)
    private BigDecimal charge;

    /**
     * 设置：游戏前金币
     *
     * @param beforeGold 游戏前金币
     */
    public void setBeforeGold(BigDecimal beforeGold) {
        this.beforeGold = beforeGold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：游戏后金币
     *
     * @param afterGold 游戏后金币
     */
    public void setAfterGold(BigDecimal afterGold) {
        this.afterGold = afterGold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：输赢金币(未扣服务费)
     *
     * @param gold 输赢金币(未扣服务费)
     */
    public void setGold(BigDecimal gold) {
        this.gold = gold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：输赢金币(已扣服务费)
     *
     * @param winGold 输赢金币(已扣服务费)
     */
    public void setWinGold(BigDecimal winGold) {
        this.winGold = winGold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 设置：服务费
     *
     * @param charge 服务费
     */
    public void setCharge(BigDecimal charge) {
        this.charge = charge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
