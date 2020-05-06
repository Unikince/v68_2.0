package com.dmg.gameconfigserver.model.vo.statement.game;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 游戏报表_游戏详情_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GameStatementDatailsCollectReq extends PageReqDTO {
    /** 游戏id,不能为空 */
    @NotNull(message = "gameId不能为空")
    private Integer gameId;
}