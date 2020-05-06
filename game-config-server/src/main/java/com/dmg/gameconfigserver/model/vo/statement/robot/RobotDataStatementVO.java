package com.dmg.gameconfigserver.model.vo.statement.robot;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:57 2020/1/6
 */
@Data
public class RobotDataStatementVO {
    private Long userId;
    /**
     * 总盈利
     */
    private BigDecimal sumWin;
    /**
     * 总下注
     */
    private BigDecimal sumBet;
    /**
     * 总赔付
     */
    private BigDecimal sumPay;
    /**
     * 返奖率
     */
    private BigDecimal returnRate;
    /**
     * 服务费
     */
    private BigDecimal serviceCharge;
}
