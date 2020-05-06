package com.dmg.gameconfigserver.service.statement;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementCollectReq;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementCollectRes;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementDayDataReq;
import com.dmg.gameconfigserver.model.vo.statement.withdraw.WithdrawStatementDayDataRes;

/**
 * 提现报表
 */
public interface WithdrawStatementService {
    /** 汇总 */
    WithdrawStatementCollectRes collect(WithdrawStatementCollectReq reqVo);

    /** 每日数据 */
    IPage<WithdrawStatementDayDataRes> dayData(WithdrawStatementDayDataReq reqVo);
}