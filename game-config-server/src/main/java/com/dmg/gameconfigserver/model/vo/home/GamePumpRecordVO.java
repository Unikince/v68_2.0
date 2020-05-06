package com.dmg.gameconfigserver.model.vo.home;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:35 2020/3/16
 */
@Data
public class GamePumpRecordVO {

    /**
     * 水池
     */
    private BigDecimal pump;
    /**
     * 记录时间
     */
    private Date recordDate;
}
