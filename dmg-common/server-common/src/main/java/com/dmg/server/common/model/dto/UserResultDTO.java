package com.dmg.server.common.model.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:09 2019/11/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResultDTO<T> {

    // 用户id
    private Integer userId;
    // 是否庄家(地主)
    private Boolean isBank = false;
    // 是否系统
    private Boolean isSys = false;
    // poker
    private List<T> pokers;
    // 牌型
    private String pokerType;
    // 牌型倍数
    private Integer mul;
    // 输赢
    private BigDecimal winLosGold;
    // 下注金额
    private BigDecimal betGold;
    // 抢
    private Integer rob;
    // 押
    private Integer pressure;
    //是否机器人
    private Boolean isRobot;

}
