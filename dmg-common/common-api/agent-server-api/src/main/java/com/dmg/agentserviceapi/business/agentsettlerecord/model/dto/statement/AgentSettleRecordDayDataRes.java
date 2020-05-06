package com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement;

import java.util.Date;

import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.common.AgentSettleCommonPlayerRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 代理结算纪录_每日数据_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AgentSettleRecordDayDataRes extends AgentSettleCommonPlayerRes {
    /** 日期 */
    private Date dayStr;
}