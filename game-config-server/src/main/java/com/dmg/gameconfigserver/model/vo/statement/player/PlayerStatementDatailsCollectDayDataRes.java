package com.dmg.gameconfigserver.model.vo.statement.player;

import com.dmg.gameconfigserver.model.vo.statement.player.common.PlayerStatementCommonGameRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 玩家报表_游戏详情_每日数据_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerStatementDatailsCollectDayDataRes extends PlayerStatementCommonGameRes {
    /** 日期字符串 */
    private String dayStr;
}