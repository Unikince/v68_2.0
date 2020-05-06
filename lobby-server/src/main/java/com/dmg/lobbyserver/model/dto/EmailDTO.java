package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/20 11:55
 * @Version V1.0
 **/
@Data
public class EmailDTO {
    private Long id;
    /**
     * 邮件类型(1:站内信息 2:优惠代码)
     */
    private Integer emailType;
    /**
     * 邮件名称
     */
    private String emailName;
    /**
     * 邮件内容信息
     */
    private String emailContent;
    /**
     * 优惠代码
     */
    private String promotionCode;
    /**
     * 是否已领取(0:未领取 1:已领取)
     */
    private Integer receive;
    /**
     * 是否阅读(0:未阅读 1:已阅读)
     */
    private Integer hasRead;
    /**
     * 过期时间
     */
    private Date expireDate;

    private PromotionCodeInfo promotionCodeInfo = new PromotionCodeInfo();

    @Data
    public static class PromotionCodeInfo{
        /**
         * 存款返利
         */
        private int depositRebate;
        /**
         * 存款返利类型 1:RMB 2:百分比
         */
        private Integer depositRebateType;
        /**
         * 所需流水
         */
        private Integer turnoverCondition;
        /**
         * 指定游戏类型
         */
        private Integer useGameType;
        /**
         * 优惠上限
         */
        private Integer promotionLimit;
        /**
         * 领取条件类型
         */
        private Integer receiveConditionType;
        /**
         * 领取条件数量限制
         */
        private Integer receiveConditionNumber;
        /**
         * 过期时间
         */
        private Date expireDate;
        /**
         * 是否已领取(0:未领取 1:已领取)
         */
        private Integer hasReceive;
    }
}