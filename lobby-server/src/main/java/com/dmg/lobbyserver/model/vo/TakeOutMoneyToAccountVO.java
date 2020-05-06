package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/20 15:58
 * @Version V1.0
 **/
@Data
public class TakeOutMoneyToAccountVO {
    private Long userId;
    private BigDecimal takeOutNum;
    private String password;
}