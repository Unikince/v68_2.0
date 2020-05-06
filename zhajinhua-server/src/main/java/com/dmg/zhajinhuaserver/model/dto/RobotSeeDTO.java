package com.dmg.zhajinhuaserver.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:07 2019/9/30
 */
@Data
public class RobotSeeDTO  implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    /**
     * 最小轮数
     */
    private Integer roundMin;
    /**
     * 最大轮数
     */
    private Integer roundMax;
    /**
     * 闷牌概率 列如100%=10000
     */
    private Integer probabilityStuffy;
    /**
     * 看牌类型 0:固定概率 1：基础概率计算
     */
    private Integer seeType;
    /**
     * 看牌概率 列如100%=10000
     */
    private Integer probabilitySee;
}
