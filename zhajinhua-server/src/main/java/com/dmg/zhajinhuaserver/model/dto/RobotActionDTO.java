package com.dmg.zhajinhuaserver.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:26 2019/9/30
 */
@Data
public class RobotActionDTO  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 牌型
     */
    private Integer cardType;
    /**
     * 最小牌
     */
    private Integer cardMin;
    /**
     * 最大牌
     */
    private Integer cardMax;
    /**
     * 是否看牌
     */
    private Boolean isSee;
    /**
     * 跟注类型 0:固定概率 1：基础概率计算
     */
    private Integer followUpType;
    /**
     * 跟注概率 列如100%=10000
     */
    private Integer probabilityFollowUp;
    /**
     * 加注类型 0:固定概率 1：基础概率计算
     */
    private Integer annotationType;

    /**
     * 加注概率 列如100%=10000
     */
    private Integer probabilityAnnotation;

    /**
     * 比牌类型 0:固定概率 1：基础概率计算
     */
    private Integer comparisonType;

    /**
     * 比牌概率 列如100%=10000
     */
    private Integer probabilityComparison;

    /**
     * 弃牌类型 0:固定概率 1：基础概率计算
     */
    private Integer discardType;
    /**
     * 弃牌概率 列如100%=10000
     */
    private Integer probabilityDiscard;
}
