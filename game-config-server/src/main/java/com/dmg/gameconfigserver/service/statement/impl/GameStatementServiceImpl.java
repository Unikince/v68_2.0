package com.dmg.gameconfigserver.service.statement.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.statement.GameStatementDao;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementCollectDataRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataDetailsReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataDetailsRes;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.game.GameStatementDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.game.common.GameStatementCommonRes;
import com.dmg.gameconfigserver.service.statement.GameStatementService;

/**
 * 游戏报表
 */
@Service
public class GameStatementServiceImpl implements GameStatementService {
    @Autowired
    private GameStatementDao dao;

    @Override
    public GameStatementCollectRes collect(GameStatementCollectReq reqVo) {
        Page<GameStatementCollectDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<GameStatementCollectDataRes> resPages = this.dao.collect(page, reqVo);

        GameStatementCollectRes resPack = new GameStatementCollectRes();
        resPack.setDatas(resPages);
        resPack.setSumWin(BigDecimal.ZERO);
        resPack.setSumBet(BigDecimal.ZERO);
        resPack.setSumPay(BigDecimal.ZERO);
        for (GameStatementCommonRes resVo : resPages.getRecords()) {
            resPack.setSumWin(resPack.getSumWin().add(resVo.getSumWin()));
            resPack.setSumBet(resPack.getSumBet().add(resVo.getSumBet()));
            resPack.setSumPay(resPack.getSumPay().add(resVo.getSumPay()));
        }

        return resPack;
    }

    @Override
    public IPage<GameStatementDayDataRes> dayData(GameStatementDayDataReq reqVo) {
        Page<GameStatementDayDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<GameStatementDayDataRes> resPages = this.dao.dayData(page, reqVo);
        return resPages;
    }

    @Override
    public IPage<GameStatementDayDataDetailsRes> dayDataDetails(GameStatementDayDataDetailsReq reqVo) {
        Page<GameStatementDayDataDetailsRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<GameStatementDayDataDetailsRes> resPages = this.dao.dayDataDetails(page, reqVo);
        return resPages;
    }

    @Override
    public IPage<GameStatementDatailsCollectRes> datailsCollect(GameStatementDatailsCollectReq reqVo) {
        Page<GameStatementDatailsCollectRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<GameStatementDatailsCollectRes> resPages = this.dao.datailsCollect(page, reqVo);
        return resPages;
    }

    @Override
    public IPage<GameStatementDatailsCollectDayDataRes> datailsCollectDayData(GameStatementDatailsCollectDayDataReq reqVo) {
        Page<GameStatementDatailsCollectDayDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<GameStatementDatailsCollectDayDataRes> resPages = this.dao.datailsCollectDayData(page, reqVo);
        return resPages;
    }
}
