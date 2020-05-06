package com.dmg.zhajinhuaserver.model.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:47 2019/10/11
 */
@Data
public class GameFileConfigDTO {
    /**
     * 游戏id
     */
    private Integer gameId;
    /**
     * 场次id
     */
    private Integer fileId;
    /**
     * 场次名称
     */
    private String fileName;
    /**
     * 是否开启 0:未开启 1:已开启
     */
    private Integer openStatus;
    /**
     * 人数上限
     */
    private Integer playerUpLimit;
    /**
     * 抽水百分比
     */
    private Integer pumpRate;
    /**
     * 基础配置id
     */
    private Integer fileBaseConfigId;
    /**
     * 底分
     */
    private BigDecimal baseScore;
    /**
     * 玩家携带下限
     */
    private BigDecimal lowerLimit;
    /**
     * 玩家携带上限
     */
    private BigDecimal upperLimit;
    /**
     * 轮数
     */
    private Integer roundMax;
    /**
     * 加注数据
     */
    private String betChips;
    /**
     * 倍数上限
     */
    private String discards;
    /**
     * 不准备踢出时间
     */
    private Integer readyTime;
}
