package com.dmg.doudizhuserver.common.platform.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * 玩家信息
 */
@Data
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 玩家id */
    private long id;
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String headImg;
    /** 性别 */
    private int sex;
    /** 金币 */
    private BigDecimal gold;
    /** 是否是机器人 */
    private boolean robot;

    /**
     * 设置：金币
     *
     * @param gold 金币
     */
    public void setGold(BigDecimal gold) {
        this.gold = gold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
