package com.dmg.gameconfigserver.model.vo.config.rate;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:47 2019/11/18
 */
@Data
public class GameExchangeRateVO{
    private Long id;
    /**
     * 汇率code
     */
    private String exchangeRateCode;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

}
