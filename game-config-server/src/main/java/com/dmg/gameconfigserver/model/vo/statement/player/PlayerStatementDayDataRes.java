package com.dmg.gameconfigserver.model.vo.statement.player;

import com.dmg.gameconfigserver.model.vo.statement.player.common.PlayerStatementCommonInfoRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 玩家报表_每日数据_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerStatementDayDataRes extends PlayerStatementCommonInfoRes {
    /** 日期字符串 */
    private String dayStr;
}