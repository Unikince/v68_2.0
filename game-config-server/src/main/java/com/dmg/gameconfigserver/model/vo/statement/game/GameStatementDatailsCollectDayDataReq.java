package com.dmg.gameconfigserver.model.vo.statement.game;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 游戏报表_游戏详情_每日数据_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GameStatementDatailsCollectDayDataReq extends PageReqDTO {
    /** 游戏id,不能为空 */
    @NotNull(message = "gameId不能为空")
    private Integer gameId;
    /** 场次id,不能为空 */
    @NotNull(message = "fileId不能为空")
    private String fileId;
    /** 查询起始时间(包含) */
    private Date startDate;
    /** 查询结束时间(包含) */
    private Date endDate;
}