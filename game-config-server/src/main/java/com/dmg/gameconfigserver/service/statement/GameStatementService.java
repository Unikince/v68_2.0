package com.dmg.gameconfigserver.service.statement;

import com.baomidou.mybatisplus.core.metadata.IPage;
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

/**
 * 游戏报表
 */
public interface GameStatementService {
    /** 汇总 */
    GameStatementCollectRes collect(GameStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<GameStatementDayDataRes> dayData(GameStatementDayDataReq reqVo);

    /** 每日数据_游戏详情 */
    IPage<GameStatementDayDataDetailsRes> dayDataDetails(GameStatementDayDataDetailsReq reqVo);

    /** 游戏详情 */
    IPage<GameStatementDatailsCollectRes> datailsCollect(GameStatementDatailsCollectReq reqVo);

    /** 游戏详情_每日数据 */
    IPage<GameStatementDatailsCollectDayDataRes> datailsCollectDayData(GameStatementDatailsCollectDayDataReq reqVo);
}