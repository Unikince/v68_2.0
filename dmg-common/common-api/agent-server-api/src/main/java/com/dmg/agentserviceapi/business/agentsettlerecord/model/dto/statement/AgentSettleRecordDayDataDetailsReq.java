package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement;

import java.util.Date;

import lombok.Data;

/**
 * 代理结算纪录_每日数据_游戏详情_请求
 */
@Data
public class AgentSettleRecordDayDataDetailsReq {
    /** 玩家id,不能为空 */
    private long playerId;
    /** 游戏时间,不能为空 */
    private Date gameDate;
}