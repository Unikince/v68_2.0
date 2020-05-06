package com.dmg.gameconfigserver.service.statement.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.statement.EveryDayStatementDao;
import com.dmg.gameconfigserver.model.bean.statement.StatementEveryDay;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementDayDataRes;
import com.dmg.gameconfigserver.service.statement.EveryDayStatementService;

import cn.hutool.core.date.DateUtil;

/**
 * 每日报表
 */
@Service
public class EveryDayStatementServiceImpl implements EveryDayStatementService {
    @Autowired
    private EveryDayStatementDao dao;

    @PostConstruct
    public void init() {
    }

    /** 更新一天的数据 */
    private synchronized void updateData(Date day) {
        java.sql.Date dayStr = new java.sql.Date(day.getTime());
        LambdaQueryWrapper<StatementEveryDay> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StatementEveryDay::getDayStr, dayStr);
        int count = this.dao.selectCount(wrapper);
        if (count == 0) {
            StatementEveryDay bean = new StatementEveryDay();
            bean.setDayStr(dayStr);
            this.dao.insert(bean);
        }
        Date beginDate = DateUtil.beginOfDay(day);
        Date endDate = DateUtil.endOfDay(day);
        
        dao.updateDataOfNewPlayerNum(beginDate, endDate, dayStr);
        dao.updateDataOfActivePlayerNum(dayStr);
        dao.updateDataOfOther(dayStr);
        
        StatementEveryDay bean = this.dao.selectOne(wrapper);
        if(bean.getNewPlayerNum()!=0){
            return;
        }
        if(bean.getActivePlayerNum()!=0){
            return;
        }
        if(bean.getSumWin().compareTo(BigDecimal.ZERO)!=0){
            return;
        }
        if(bean.getSumBet().compareTo(BigDecimal.ZERO)!=0){
            return;
        }
        if(bean.getSumPay().compareTo(BigDecimal.ZERO)!=0){
            return;
        }
        if(bean.getCharge().compareTo(BigDecimal.ZERO)!=0){
            return;
        }
        if(bean.getSumRecharge().compareTo(BigDecimal.ZERO)!=0){
            return;
        }
        if(bean.getSumWithdraw().compareTo(BigDecimal.ZERO)!=0){
            return;
        }
        if(bean.getDiffRechargeSubWithdraw().compareTo(BigDecimal.ZERO)!=0){
            return;
        }
        if(bean.getArpu().compareTo(BigDecimal.ZERO)!=0){
            return;
        }
        dao.deleteById(bean.getId());
    }

    @Override
    public void yesterdayData() {
        Date yestoday = DateUtil.beginOfDay(DateUtil.yesterday());
        updateData(yestoday);
    }

    @Override
    public EveryDayStatementCollectRes collect(EveryDayStatementCollectReq reqVo) {
        updateData(new Date());
        EveryDayStatementCollectRes resPages = this.dao.collect(reqVo);
        resPages.setArpu(resPages.getSumRecharge().divide(new BigDecimal(resPages.getRegisterNum()),2, BigDecimal.ROUND_HALF_UP));
        return resPages;
    }
    
    @Override
    public IPage<EveryDayStatementDayDataRes> dayData(EveryDayStatementDayDataReq reqVo) {
        updateData(new Date());
        Page<EveryDayStatementDayDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<EveryDayStatementDayDataRes> resPages = this.dao.dayData(page, reqVo);
        return resPages;
    }
}
