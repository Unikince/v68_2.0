package com.dmg.bairenniuniuserver.model.dto;

import com.dmg.bairenniuniuserver.common.model.Poker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/25 13:46
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameResultDetailDTO {
    // 用户id
    private Integer userId;
    // 是否庄家
    private Boolean isBank = false;
    // 是否系统庄家
    private Boolean isSysBank = true;
    // 牌桌信息 key:tableIndex
    private Map<String,TableInfo> tableInfoMap;
    // 下注金额
    private BigDecimal betGold;
    // 输赢
    private BigDecimal winLosGold;

}