package com.dmg.gameconfigserver.model.vo.statement.recharge;

import com.dmg.gameconfigserver.model.vo.statement.recharge.common.RechargeStatementCommonRes;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 充值报表_充值详情_返回
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeStatementDatailsCollectRes extends RechargeStatementCommonRes {
    /** 充值渠道 */
    private String channel;
}