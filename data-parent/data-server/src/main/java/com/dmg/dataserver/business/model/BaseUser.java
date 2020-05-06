package com.dmg.dataserver.business.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户基础数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseUser {
    /** 玩家id */
    private long id;
    /** 昵称 */
    private String nickname;
    /** 头像 */
    private String headImage;
    /** 性别 */
    private int sex;
    /** 金币 */
    private BigDecimal gold;

    /**
     * 设置：金币
     *
     * @param gold 金币
     */
    public void setGold(BigDecimal gold) {
        this.gold = gold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
