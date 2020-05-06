package com.dmg.gameconfigserver.model.vo.statement.player;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 玩家报表_汇总_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerStatementCollectReq extends PageReqDTO {
    /** 玩家id,可以为空 */
    private Long playerId;
}