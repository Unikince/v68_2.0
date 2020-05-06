package com.dmg.gameconfigserver.service.statement;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.everyday.EveryDayStatementDayDataRes;

/**
 * 每日报表
 */
public interface EveryDayStatementService {
    /** 每日自动生成昨日数据 */
    public void yesterdayData();
    
    /** 汇总 */
    EveryDayStatementCollectRes collect(EveryDayStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<EveryDayStatementDayDataRes> dayData(EveryDayStatementDayDataReq reqVo);
}