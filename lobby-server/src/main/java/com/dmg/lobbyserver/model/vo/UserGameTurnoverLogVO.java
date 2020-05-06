package com.dmg.lobbyserver.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:21 2019/11/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGameTurnoverLogVO {

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 游戏类型
     */
    private Integer gameType;
    /**
     * 流水数量
     */
    private BigDecimal turnoverNumber;
    /**
     * 派彩数量
     */
    private BigDecimal payoutNumber;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 游戏前金币
     */
    private BigDecimal beforeGameGold;
    /**
     * 游戏后金币
     */
    private BigDecimal afterGameGold;
}
