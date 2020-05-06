package com.dmg.gameconfigserver.model.vo.statement.player;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 玩家报表_游戏详情_每日数据_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerStatementDatailsCollectDayDataReq extends PageReqDTO {
    /** 玩家id,不能为空 */
    @NotNull(message = "playerId不能为空")
    private Integer playerId;
    /** 游戏id,不能为空 */
    @NotNull(message = "gameId不能为空")
    private Integer gameId;
    /** 查询起始时间(包含) */
    private Date startDate;
    /** 查询结束时间(包含) */
    private Date endDate;
}