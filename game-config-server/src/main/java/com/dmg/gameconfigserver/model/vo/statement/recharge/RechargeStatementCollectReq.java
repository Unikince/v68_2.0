package com.dmg.gameconfigserver.model.vo.statement.recharge;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 充值报表_汇总_请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RechargeStatementCollectReq extends PageReqDTO {
    /** 充值类型(所有类型、人工充值、渠道充值) */
    private String type;
    /** 充值渠道(type=渠道充值时传入，参考/game-config-api//finance/pay-channel/channelGroup) */
    private String channel;
}