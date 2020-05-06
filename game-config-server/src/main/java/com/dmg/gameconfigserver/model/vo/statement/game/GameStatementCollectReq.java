package com.dmg.gameconfigserver.model.vo.statement.game;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 游戏报表_汇总_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GameStatementCollectReq extends PageReqDTO {
    /** 游戏id,可以为空,为空则查询所有游戏 */
    private Integer gameId;
}