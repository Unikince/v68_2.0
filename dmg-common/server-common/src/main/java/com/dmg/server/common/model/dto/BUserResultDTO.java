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
public class BUserResultDTO<T> {

    // table
    private Integer table;
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
    // 下注
    private BigDecimal betsGold;

}
