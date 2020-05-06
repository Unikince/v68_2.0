package com.dmg.niuniuserver.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:47 2019/11/18
 */
@Data
public class GameExchangeRateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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
