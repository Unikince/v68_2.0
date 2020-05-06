package com.dmg.gameconfigserver.service.statement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.statement.WithdrawStatementDao;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementDayDataRes;
import com.dmg.gameconfigserver.service.statement.WithdrawStatementService;

/**
 * 提款报表
 */
@Service
public class WithdrawStatementServiceImpl implements WithdrawStatementService {
    @Autowired
    private WithdrawStatementDao dao;

    @Override
    public WithdrawStatementCollectRes collect(WithdrawStatementCollectReq reqVo) {
        WithdrawStatementCollectRes resPages = this.dao.collect(reqVo);
        return resPages;
    }
    
    @Override
    public IPage<WithdrawStatementDayDataRes> dayData(WithdrawStatementDayDataReq reqVo) {
        Page<WithdrawStatementDayDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<WithdrawStatementDayDataRes> resPages = this.dao.dayData(page, reqVo);
        return resPages;
    }
}
