package com.dmg.agentserviceapi.business.transferaccountrecord.model.pojo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 转账纪录
 */
@Data
public class TransferAccountRecord {
    /** 逻辑ID */
    private long id;
    /** 订单号 */
    private String orderId;
    /** 转账玩家id */
    private long sourceId;
    /** 转账玩家昵称 */
    private String sourceName;
    /** 接收玩家id */
    private long targetId;
    /** 接收玩家昵称 */
    private String targetName;
    /** 转账金额 */
    private BigDecimal sendAmount;
    /** 到账金额 */
    private BigDecimal recvAmount;
    /** 手续费 */
    private BigDecimal pump;
    /** 状态(1开始,2完成,3撤回) */
    private int status;
    /** 创建时间 */
    private Date createDate;
    /** 完成时间(玩家接收或撤回) */
    private Date finishDate;
    /** 代理总充值 */
    private BigDecimal totalAgentRecharge;
    /** 代理总流水 */
    private BigDecimal totalAgentWater;
}
