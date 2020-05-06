package com.dmg.gameconfigserver.model.vo.statement.everyday;

import com.dmg.gameconfigserver.model.vo.statement.everyday.common.EveryDayStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 每日报表_汇总_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EveryDayStatementCollectRes extends EveryDayStatementCommonRes {
    /** 注册人数 */
    private int registerNum;
}