package com.dmg.gameconfigserver.model.vo.statement.player.common;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 玩家报表_游戏_公用_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerStatementCommonGameRes extends PlayerStatementCommonRes {
    /** 游戏ID */
    private long gameId;
    /** 游戏名称 */
    private String gameName;
    /** 总盈利 */
    private BigDecimal sumWin;
}
