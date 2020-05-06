package com.dmg.agentserver.business.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.dmg.agentserver.business.model.bo.AgentSettleCalculateBo;
import com.dmg.agentserver.business.model.bo.AgentSettleGameDetailBo;
import com.dmg.agentserver.business.model.bo.AgentSettleRecordBo;

/**
 * 代理结算-计算
 */
@DS("v68")
@Mapper
public interface AgentSettleCalculateDao {
    /**
     * 获取昨日结算原始数据
     * 偷懒不想写映射对象，借用
     */
    List<AgentSettleCalculateBo> getYesterdaySettleRecord();

    /**
     * 获取所有游戏信息
     * 偷懒不想写映射对象，借用
     */
    @DS("dmg_admin")
    List<AgentSettleCalculateBo> getAllGameInfo();

    /**
     * 查询指定日期的纪录
     */
    List<AgentSettleRecordBo> getRecordByDate(Date date);

    /**
     * 批量插入纪录
     */
    void insertBatchRecord(Collection<AgentSettleRecordBo> pos);

    /**
     * 批量插入游戏详情
     */
    void insertBatchDetail(Collection<AgentSettleGameDetailBo> pos);
}
