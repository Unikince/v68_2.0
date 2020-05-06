package com.dmg.gameconfigserver.model.vo.statement.game;

import com.dmg.gameconfigserver.model.vo.statement.game.common.GameStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 游戏报表_游戏详情_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GameStatementDatailsCollectRes extends GameStatementCommonRes {
    /** 场次id */
    private String fileId;
    /** 场次名称 */
    private String fileName;
}