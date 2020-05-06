package com.dmg.gameconfigserver.model.vo.statement.player;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 玩家报表_游戏详情_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerStatementDatailsCollectReq extends PageReqDTO {
    /** 玩家id,不能为空 */
    @NotNull(message = "playerId不能为空")
    private Integer playerId;
}