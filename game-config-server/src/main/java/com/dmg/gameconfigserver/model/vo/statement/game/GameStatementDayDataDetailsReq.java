package com.dmg.gameconfigserver.model.vo.statement.game;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 游戏报表_每日数据_游戏详情_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GameStatementDayDataDetailsReq extends PageReqDTO {
    /** 游戏id,不能为空 */
    @NotNull(message = "gameId不能为空")
    private Integer gameId;
    /** 游戏时间 */
    @NotNull(message = "gameDate不能为空")
    private Date gameDate;
}