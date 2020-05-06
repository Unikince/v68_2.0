package com.dmg.gameconfigserver.model.vo.statement.recharge;

import com.dmg.gameconfigserver.model.vo.statement.recharge.common.RechargeStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 充值报表_游戏详情_每日数据_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeStatementDatailsCollectDayDataRes extends RechargeStatementCommonRes {
    /** 日期字符串 */
    private String dayStr;
    /** 充值渠道 */
    private String channel;
}