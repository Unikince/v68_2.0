package com.dmg.gameconfigserver.model.dto.config;

import lombok.Data;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:53 2019/10/14
 */
@Data
public class GameControlConfigDTO {
    /**
     * 基础配置id
     */
    private Integer fileBaseConfigId;
    /**
     * 发牌权重最小值
     */
    private Integer minDealCards;
    /**
     * 发牌权重最大值
     */
    private Integer maxDealCards;
    /**
     * 跟注金额最小值
     */
    private Integer minFollowUp;
    /**
     * 跟注金额最大值
     */
    private Integer maxFollowUp;
    /**
     * 炸弹个数最小值
     */
    private Integer minBomb;
    /**
     * 炸弹个数最小值
     */
    private Integer maxBomb;
    /**
     * 权重定义
     */
    private String weight;
}
