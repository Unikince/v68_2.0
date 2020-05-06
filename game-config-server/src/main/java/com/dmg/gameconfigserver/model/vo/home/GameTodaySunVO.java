package com.dmg.gameconfigserver.model.vo.home;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 14:34 2020/3/17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameTodaySunVO {
    /**
     * 活跃人数
     */
    private Long activeNum;
    /**
     * 新增人数
     */
    private Long registerNum;
    /**
     * 首充人数
     */
    private Long firstRechargeNum;
    /**
     * 首充金额
     */
    private BigDecimal sumFirstRecharge;
    /**
     * 充值总额
     */
    private BigDecimal sumRecharge;
    /**
     * 提款总额
     */
    private BigDecimal sumDrawing;
    /**
     * 提存差
     */
    private BigDecimal differReDr;
    /**
     * 盈利
     */
    private BigDecimal profit;
    /**
     * 服务费
     */
    private BigDecimal service;

    public BigDecimal getDifferReDr() {
        return this.sumRecharge.subtract(this.sumDrawing);
    }
}
