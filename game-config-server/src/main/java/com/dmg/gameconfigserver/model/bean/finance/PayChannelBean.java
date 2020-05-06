package com.dmg.gameconfigserver.model.bean.finance;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 支付渠道
 */
@Data
@TableName("pay_channel")
public class PayChannelBean {
    /** ID */
    private int id;
    /** 支付渠道 */
    private String channel;
    /** 渠道名称 */
    private String channelName;
    /** 展示顺序 */
    private int sort;
    /** 充值额度 */
    private String pays;
    /** 日充值限额 */
    private BigDecimal payAmountMax;
    /** VIP等级限制 */
    private int vipLevel;
    /** 状态 */
    private boolean status;
}
