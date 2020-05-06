package com.dmg.data.common.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 用户数据接收 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecvDto {
    /** 状态码(0:成功,-1未知错误,1玩家未找到) */
    private int code;
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
    /** 游戏id */
    private int gameId;
    /** 房间等级 */
    private int roomLevel;
    /** 房间id */
    private int roomId;

    /**
     * 设置：金币
     *
     * @param gold 金币
     */
    public void setGold(BigDecimal gold) {
        this.gold = gold.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
