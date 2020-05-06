package com.dmg.agentserver.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.agentserver.business.dao.AgentSettleStatementDao;
import com.dmg.agentserviceapi.business.agentsettlerecord.model.dto.statement.AgentSettleRecordCollectExtraRes;
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
import com.dmg.agentserviceapi.core.pack.PageAndExtraPackageRes;
import com.dmg.agentserviceapi.core.pack.PagePackageRes;
import com.dmg.agentserviceapi.core.page.PageReq;
import com.dmg.agentserviceapi.core.page.PageRes;

import cn.hutool.core.bean.BeanUtil;

/**
 * 代理结算-统计
 */
@Service
public class AgentSettleStatementService {
    @Autowired
    private AgentSettleStatementDao dao;

    /**
     * 汇总
     */
    public PageAndExtraPackageRes<List<AgentSettleRecordCollectRes>, AgentSettleRecordCollectExtraRes> collect(PageReq pageReq, AgentSettleRecordCollectReq reqVo) {
        Page<AgentSettleRecordCollectRes> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        IPage<AgentSettleRecordCollectRes> pagePos = this.dao.collect(page, reqVo);
        PageAndExtraPackageRes<List<AgentSettleRecordCollectRes>, AgentSettleRecordCollectExtraRes> result = new PageAndExtraPackageRes<>();
        PageRes pageRes = new PageRes();
        BeanUtil.copyProperties(pagePos, pageRes);
        result.setData(pagePos.getRecords());
        result.setPage(pageRes);
        if (reqVo.getPlayerId() == 0) {
            AgentSettleRecordCollectExtraRes extraRes = new AgentSettleRecordCollectExtraRes();
            extraRes.setAllBrokerage(this.dao.collectAllBrokerage());
            result.setExtra(extraRes);
        }
        return result;
    }

    /**
     * 每日数据
     */
    public PagePackageRes<List<AgentSettleRecordDayDataRes>> dayData(PageReq pageReq, AgentSettleRecordDayDataReq reqVo) {
        Page<AgentSettleRecordDayDataRes> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        IPage<AgentSettleRecordDayDataRes> pagePos = this.dao.dayData(page, reqVo);
        PageRes pageRes = new PageRes();
        BeanUtil.copyProperties(pagePos, pageRes);
        PagePackageRes<List<AgentSettleRecordDayDataRes>> result = new PagePackageRes<>();
        result.setData(pagePos.getRecords());
        result.setPage(pageRes);
        return result;
    }

    /**
     * 每日数据_游戏详情
     */
    public PagePackageRes<List<AgentSettleRecordDayDataDetailsRes>> dayDataDetails(PageReq pageReq, AgentSettleRecordDayDataDetailsReq reqVo) {
        Page<AgentSettleRecordDayDataDetailsRes> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        IPage<AgentSettleRecordDayDataDetailsRes> pagePos = this.dao.dayDataDetails(page, reqVo);
        PageRes pageRes = new PageRes();
        BeanUtil.copyProperties(pagePos, pageRes);
        PagePackageRes<List<AgentSettleRecordDayDataDetailsRes>> result = new PagePackageRes<>();
        result.setData(pagePos.getRecords());
        result.setPage(pageRes);
        return result;
    }

    /**
     * 游戏详情
     */
    public PagePackageRes<List<AgentSettleRecordDatailsCollectRes>> datailsCollect(PageReq pageReq, AgentSettleRecordDatailsCollectReq reqVo) {
        Page<AgentSettleRecordDatailsCollectRes> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        IPage<AgentSettleRecordDatailsCollectRes> pagePos = this.dao.datailsCollect(page, reqVo);
        PageRes pageRes = new PageRes();
        BeanUtil.copyProperties(pagePos, pageRes);
        PagePackageRes<List<AgentSettleRecordDatailsCollectRes>> result = new PagePackageRes<>();
        result.setData(pagePos.getRecords());
        result.setPage(pageRes);
        return result;
    }

    /**
     * 游戏详情_每日数据
     */
    public PagePackageRes<List<AgentSettleRecordDatailsCollectDayDataRes>> datailsCollectDayData(PageReq pageReq, AgentSettleRecordDatailsCollectDayDataReq reqVo) {
        Page<AgentSettleRecordDatailsCollectDayDataRes> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        IPage<AgentSettleRecordDatailsCollectDayDataRes> pagePos = this.dao.datailsCollectDayData(page, reqVo);
        PageRes pageRes = new PageRes();
        BeanUtil.copyProperties(pagePos, pageRes);
        PagePackageRes<List<AgentSettleRecordDatailsCollectDayDataRes>> result = new PagePackageRes<>();
        result.setData(pagePos.getRecords());
        result.setPage(pageRes);
        return result;
    }
}