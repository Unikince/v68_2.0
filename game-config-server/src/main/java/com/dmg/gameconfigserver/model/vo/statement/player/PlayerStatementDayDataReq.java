package com.dmg.gameconfigserver.model.vo.statement.player;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 玩家报表_每日数据_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlayerStatementDayDataReq extends PageReqDTO {
    /** 玩家id,不能为空 */
    @NotNull(message = "playerId不能为空")
    private Integer playerId;
    /** 查询起始时间(包含) */
    private Date startDate;
    /** 查询结束时间(包含) */
    private Date endDate;
}