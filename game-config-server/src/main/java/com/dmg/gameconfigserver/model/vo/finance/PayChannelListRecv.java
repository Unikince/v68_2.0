package com.dmg.gameconfigserver.model.vo.finance;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付渠道
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PayChannelListRecv extends PageReqDTO {
    /** 支付渠道 */
    private String channel;
    /** 状态 */
    private Boolean status;
}
