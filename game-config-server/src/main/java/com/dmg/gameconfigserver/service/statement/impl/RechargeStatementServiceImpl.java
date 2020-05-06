package com.dmg.gameconfigserver.service.statement.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.statement.RechargeStatementDao;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDayDataRes;
import com.dmg.gameconfigserver.service.statement.RechargeStatementService;

/**
 * 充值报表
 */
@Service
public class RechargeStatementServiceImpl implements RechargeStatementService {
    @Autowired
    private RechargeStatementDao dao;

    @Override
    public List<RechargeStatementCollectRes> collect(RechargeStatementCollectReq reqVo) {

        if (StringUtils.equals(reqVo.getChannel(), "所有渠道")) {
            reqVo.setChannel(null);
        } else if (StringUtils.isBlank(reqVo.getChannel())) {
            reqVo.setChannel(null);
        }
        List<RechargeStatementCollectRes> result = new ArrayList<>();
        if (StringUtils.equals(reqVo.getType(), "人工充值")) {
            result.add(this.dao.collectByPersion(reqVo));
        } else if (StringUtils.equals(reqVo.getType(), "渠道充值")) {
            result.add(this.dao.collectByPlatform(reqVo));
        } else {
            result.add(this.dao.collectByPersion(reqVo));
            result.add(this.dao.collectByPlatform(reqVo));
        }
        return result;
    }

    @Override
    public IPage<RechargeStatementDayDataRes> dayData(RechargeStatementDayDataReq reqVo) {
        Page<RechargeStatementDayDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<RechargeStatementDayDataRes> resPages = null;
        if (StringUtils.equals(reqVo.getType(), "人工充值")) {
            resPages = this.dao.dayDataByPersion(page, reqVo);
        } else if (StringUtils.equals(reqVo.getType(), "渠道充值")) {
            resPages = this.dao.dayDataByPlatform(page, reqVo);
        }
        return resPages;
    }

    @Override
    public IPage<RechargeStatementDatailsCollectRes> datailsCollect(RechargeStatementDatailsCollectReq reqVo) {
        Page<RechargeStatementDatailsCollectRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<RechargeStatementDatailsCollectRes> resPages = null;
        resPages = this.dao.datailsCollectByPlatform(page, reqVo);
        return resPages;
    }

    @Override
    public IPage<RechargeStatementDatailsCollectDayDataRes> datailsCollectDayData(RechargeStatementDatailsCollectDayDataReq reqVo) {
        Page<RechargeStatementDatailsCollectDayDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<RechargeStatementDatailsCollectDayDataRes> resPages = null;
        resPages = this.dao.datailsCollectDayDataByPlatform(page, reqVo);
        return resPages;
    }

}
