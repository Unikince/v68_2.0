package com.dmg.lobbyserver.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/24 14:54
 * @Version V1.0
 **/
@Data
public class CreateOrderVO {
    private long userId;
    private long itemMallId;
    private long itemId;
    private BigDecimal itemNum;
    private BigDecimal originalPrice;
    private String platformId;
}