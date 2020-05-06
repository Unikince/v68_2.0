package com.dmg.gameconfigserver.model.vo.statement.recharge;

import com.dmg.gameconfigserver.model.vo.statement.recharge.common.RechargeStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 充值报表_每日数据_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeStatementDayDataRes extends RechargeStatementCommonRes {
    /** 日期字符串 */
    private String dayStr;
}