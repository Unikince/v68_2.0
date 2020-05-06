package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement;

import java.util.Date;

import lombok.Data;

/**
 * 代理结算纪录_游戏详情_每日数据_请求
 */
@Data
public class AgentSettleRecordDatailsCollectDayDataReq {
    /** 玩家id,不能为空 */
    private long playerId;
    /** 游戏id,不能为空 */
    private int gameId;
    /** 查询起始时间(包含) */
    private Date startDate;
    /** 查询结束时间(包含) */
    private Date endDate;
}