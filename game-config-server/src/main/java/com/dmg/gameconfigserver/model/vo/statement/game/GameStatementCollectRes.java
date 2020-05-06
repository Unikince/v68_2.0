package com.dmg.gameconfigserver.model.vo.statement.game;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;

import lombok.Data;

/**
 * 游戏报表_汇总_返回
 */
@Data
public class GameStatementCollectRes {
    /** 总盈利 */
    private BigDecimal sumWin;
    /** 总下注 */
    private BigDecimal sumBet;
    /** 总赔付 */
    private BigDecimal sumPay;
    /** 报表数据 */
    private IPage<GameStatementCollectDataRes> datas;
}