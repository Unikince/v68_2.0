package com.dmg.gameconfigserver.service.statement.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dmg.gameconfigserver.dao.statement.PlayerStatementDao;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerChargeAndWithdrawInfo;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataDetailsReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataDetailsRes;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.player.PlayerStatementDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.player.common.PlayerStatementCommonInfoRes;
import com.dmg.gameconfigserver.model.vo.statement.player.common.PlayerStatementCommonRes;
import com.dmg.gameconfigserver.service.statement.PlayerStatementService;

/**
 * 玩家报表
 */
@Service
public class PlayerStatementServiceImpl implements PlayerStatementService {
    @Autowired
    private PlayerStatementDao dao;

    @Override
    public PlayerChargeAndWithdrawInfo getPlayerChargeAndWithdrawInfo(long playerId) {
        PlayerChargeAndWithdrawInfo result = this.dao.getPlayerChargeAndWithdrawInfo(playerId);
        if (result == null) {
            result = new PlayerChargeAndWithdrawInfo();
            result.setSumRecharge(BigDecimal.ZERO);
            result.setSumWithdraw(BigDecimal.ZERO);
            result.setDiffRechargeSubWithdraw(BigDecimal.ZERO);
        }
        return result;
    }

    @Override
    public PlayerStatementCollectRes collect(PlayerStatementCollectReq reqVo) {
        Page<PlayerStatementCommonRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<PlayerStatementCommonInfoRes> resPages = this.dao.collect(page, reqVo);

        PlayerStatementCollectRes resPack = new PlayerStatementCollectRes();
        resPack.setAllRegister((int) resPages.getTotal());
        resPack.setDatas(resPages);
        resPack.setAllRegister(resPages.getTotal());

        return resPack;
    }

    @Override
    public IPage<PlayerStatementDayDataRes> dayData(PlayerStatementDayDataReq reqVo) {
        Page<PlayerStatementDayDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<PlayerStatementDayDataRes> resPages = this.dao.dayData(page, reqVo);
        return resPages;
    }

    @Override
    public IPage<PlayerStatementDayDataDetailsRes> dayDataDetails(PlayerStatementDayDataDetailsReq reqVo) {
        Page<PlayerStatementDayDataDetailsRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<PlayerStatementDayDataDetailsRes> resPages = this.dao.dayDataDetails(page, reqVo);
        return resPages;
    }

    @Override
    public IPage<PlayerStatementDatailsCollectRes> datailsCollect(PlayerStatementDatailsCollectReq reqVo) {
        Page<PlayerStatementDatailsCollectRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<PlayerStatementDatailsCollectRes> resPages = this.dao.datailsCollect(page, reqVo);
        return resPages;
    }

    @Override
    public IPage<PlayerStatementDatailsCollectDayDataRes> datailsCollectDayData(PlayerStatementDatailsCollectDayDataReq reqVo) {
        Page<PlayerStatementDatailsCollectDayDataRes> page = new Page<>(reqVo.getCurrent(), reqVo.getSize());
        IPage<PlayerStatementDatailsCollectDayDataRes> resPages = this.dao.datailsCollectDayData(page, reqVo);
        return resPages;
    }
}
