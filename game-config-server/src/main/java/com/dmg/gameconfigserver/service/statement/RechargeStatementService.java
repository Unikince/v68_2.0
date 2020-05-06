package com.dmg.gameconfigserver.service.statement;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectDayDataRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDatailsCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.recharge.RechargeStatementDayDataRes;

/**
 * 充值报表
 */
public interface RechargeStatementService {
    /** 汇总 */
    List<RechargeStatementCollectRes> collect(RechargeStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<RechargeStatementDayDataRes> dayData(RechargeStatementDayDataReq reqVo);

    /** 游戏详情 */
    IPage<RechargeStatementDatailsCollectRes> datailsCollect(RechargeStatementDatailsCollectReq reqVo);

    /** 游戏详情_每日数据 */
    IPage<RechargeStatementDatailsCollectDayDataRes> datailsCollectDayData(RechargeStatementDatailsCollectDayDataReq reqVo);
}