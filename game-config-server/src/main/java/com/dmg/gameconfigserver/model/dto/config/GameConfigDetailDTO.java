package com.dmg.gameconfigserver.model.dto.config;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/8 16:20
 * @Version V1.0
 **/
@Data
public class GameConfigDetailDTO {
    private Integer fileBaseConfigId;
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
    private BigDecimal pumpRate;
    /**
     * 当前玩家人数
     */
    private Integer curPlayerNumber;
    /**
     * 当前机器人数
     */
    private Integer curRobotNumber;
    /**
     * 当前库存
     */
    private BigDecimal winGold;

}