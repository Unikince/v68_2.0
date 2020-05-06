package com.dmg.gameconfigserver.service.statement;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

/**
 * 玩家报表
 */
public interface PlayerStatementService {
    /** 玩家详情获取玩家充值提款信息 */
    PlayerChargeAndWithdrawInfo getPlayerChargeAndWithdrawInfo(long playerId);

    /** 汇总 */
    PlayerStatementCollectRes collect(PlayerStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<PlayerStatementDayDataRes> dayData(PlayerStatementDayDataReq reqVo);

    /** 每日数据_游戏详情 */
    IPage<PlayerStatementDayDataDetailsRes> dayDataDetails(PlayerStatementDayDataDetailsReq reqVo);

    /** 游戏详情 */
    IPage<PlayerStatementDatailsCollectRes> datailsCollect(PlayerStatementDatailsCollectReq reqVo);

    /** 游戏详情_每日数据 */
    IPage<PlayerStatementDatailsCollectDayDataRes> datailsCollectDayData(PlayerStatementDatailsCollectDayDataReq reqVo);
}
