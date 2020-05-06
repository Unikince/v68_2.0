package com.dmg.zhajinhuaserver.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:51 2019/9/30
 */
@Data
public class RobotProbabilityDTO  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 轮数基础概率 列如100%=10000
     */
    private Integer probabilityRound;
    /**
     * 看牌加注人数基础概率 列如100%=10000
     */
    private Integer probabilitySeeAnnotation;
    /**
     * 看牌跟注人数基础概率 列如100%=10000
     */
    private Integer probabilitySeeFollowUp;
    /**
     * 比牌赢人数基础概率 列如100%=10000
     */
    private Integer probabilityComparisonWin;
}
