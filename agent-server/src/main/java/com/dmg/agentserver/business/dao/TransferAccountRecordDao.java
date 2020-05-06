package com.dmg.agentserver.business.dao;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dmg.agentserver.business.model.po.TransferAccountRecordPo;

/**
 * 转账纪录
 */
@Mapper
public interface TransferAccountRecordDao extends BaseMapper<TransferAccountRecordPo> {

    /**
     * 获取用户昵称
     */
    public String getUserNickByUserId(Long userId);

    /**
     * 获取代理总充值
     */
    public BigDecimal getTotalAgentRecharge(Long sourceId);

    /**
     * 获取代理总流水
     */
    public BigDecimal getTotalAgentWater(Long sourceId);

    /**
     * 获取代理今日转账次数
     */
    public int getCountOfTimesToday(Long sourceId);

    /**
     * 获取代理今日转账金额
     */
    public BigDecimal getAmoutOfPayToday(Long sourceId);

}
