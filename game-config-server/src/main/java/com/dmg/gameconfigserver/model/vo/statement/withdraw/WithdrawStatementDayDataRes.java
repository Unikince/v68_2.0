package com.dmg.gameconfigserver.model.vo.statement.withdraw;

import com.dmg.gameconfigserver.model.vo.statement.withdraw.common.WithdrawStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 提款报表_每日数据_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WithdrawStatementDayDataRes extends WithdrawStatementCommonRes {
    /** 日期字符串 */
    private String dayStr;
}