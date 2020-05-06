package com.dmg.gameconfigserver.model.vo.statement.game;

import com.dmg.gameconfigserver.model.vo.statement.game.common.GameStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 游戏报表_每日数据_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GameStatementDayDataRes extends GameStatementCommonRes {
    /** 日期字符串 */
    private String dayStr;
    /** 玩家人数 */
    private Integer playerNum;
}