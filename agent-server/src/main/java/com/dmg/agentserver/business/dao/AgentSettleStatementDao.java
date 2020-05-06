package com.dmg.agentserver.business.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordCollectReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordCollectRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDatailsCollectDayDataReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDatailsCollectDayDataRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDatailsCollectReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDatailsCollectRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDayDataDetailsReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDayDataDetailsRes;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDayDataReq;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordDayDataRes;

/**
 * 代理结算--统计
 */
@Mapper
public interface AgentSettleStatementDao {
    /**
     * 汇总
     */
    IPage<AgentSettleRecordCollectRes> collect(Page<AgentSettleRecordCollectRes> page, @Param("reqVo") AgentSettleRecordCollectReq reqVo);

    /**
     * 汇总总共返佣
     */
    BigDecimal collectAllBrokerage();

    /**
     * 每日数据
     */
    IPage<AgentSettleRecordDayDataRes> dayData(Page<AgentSettleRecordDayDataRes> page, @Param("reqVo") AgentSettleRecordDayDataReq reqVo);

    /**
     * 每日数据_游戏详情
     */
    IPage<AgentSettleRecordDayDataDetailsRes> dayDataDetails(Page<AgentSettleRecordDayDataDetailsRes> page, @Param("reqVo") AgentSettleRecordDayDataDetailsReq reqVo);

    /**
     * 游戏详情
     */
    IPage<AgentSettleRecordDatailsCollectRes> datailsCollect(Page<AgentSettleRecordDatailsCollectRes> page, @Param("reqVo") AgentSettleRecordDatailsCollectReq reqVo);

    /**
     * 游戏详情_每日数据
     */
    IPage<AgentSettleRecordDatailsCollectDayDataRes> datailsCollectDayData(Page<AgentSettleRecordDatailsCollectDayDataRes> page, @Param("reqVo") AgentSettleRecordDatailsCollectDayDataReq reqVo);

}
