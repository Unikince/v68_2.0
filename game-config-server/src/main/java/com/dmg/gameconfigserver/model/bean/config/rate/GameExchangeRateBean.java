package com.dmg.gameconfigserver.model.bean.config.rate;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dmg.gameconfigserver.model.bean.CommonBean;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 13:47 2019/11/18
 */
@Data
@TableName("t_dmg_game_exchange_rate_config")
public class GameExchangeRateBean extends CommonBean implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 汇率code
     */
    private String exchangeRateCode;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;

}
