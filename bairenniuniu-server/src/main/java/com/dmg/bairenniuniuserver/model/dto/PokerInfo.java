package com.dmg.bairenniuniuserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/9/28 14:05
 * @Version V1.0
 **/
@Data
@AllArgsConstructor
public class PokerInfo {
    /*输赢钱*/
    BigDecimal gold;
    /*是否通杀或通赔*/
    boolean allWinOrLose;
}