package com.dmg.lobbyserver.controller.pay.jqpay;

import lombok.Data;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/23 10:25
 * @Version V1.0
 **/
@Data
public class PayCallbackVO {
    //实际支付金额
    private String amount;
    //货币单位
    private String currency;
    //响应编码 默认为0
    private String code;
    //平台订单编号
    private String orderId;
    //接入方商户编号
    private String merchantId;
    //接入方游戏编号
    private String appId;
    //接入方用户id
    private String outUserId;
    //支付通道支付单号
    private String passageTradeNo;
    // 支付时间
    private String successTime;
    //接入方商品ID
    private String commodityId;
    //接入方订单号
    private String outOrderNo;
    //接入方订单价格（美元）
    private String outPayAmount;
    //1美元兑当地货币汇率
    private String exchangeRate;
    //实际支付金额（美元）
    private String amountDollar;
    //md签名，参考签名算法章节
    private String sign;
}