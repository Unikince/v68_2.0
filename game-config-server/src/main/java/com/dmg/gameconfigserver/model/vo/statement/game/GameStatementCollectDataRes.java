package com.dmg.gameconfigserver.model.vo.statement.game;

import com.dmg.gameconfigserver.model.vo.statement.game.common.GameStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 游戏报表_汇总_数据返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GameStatementCollectDataRes extends GameStatementCommonRes {
    /** 玩家人数 */
    private Integer playerNum;
}