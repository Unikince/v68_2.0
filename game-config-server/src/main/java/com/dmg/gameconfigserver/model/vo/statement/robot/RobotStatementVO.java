package com.dmg.gameconfigserver.model.vo.statement.robot;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:50 2020/1/6
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RobotStatementVO {
    /**
     * 总盈利
     */
    private BigDecimal sumWin = BigDecimal.ZERO;
    /**
     * 总下注
     */
    private BigDecimal sumBet = BigDecimal.ZERO;
    /**
     * 总赔付
     */
    private BigDecimal sumPay = BigDecimal.ZERO;
    /**
     * 平均返奖率
     */
    private BigDecimal returnRate = BigDecimal.ZERO;
    /**
     * 报表数据
     */
    private IPage<RobotDataStatementVO> datas;
}
