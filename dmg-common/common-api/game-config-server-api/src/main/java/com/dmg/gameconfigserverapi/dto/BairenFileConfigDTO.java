package com.dmg.gameconfigserverapi.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/8 18:35
 * @Version V1.0
 **/
@Data
public class BairenFileConfigDTO {
    /**
     * 基础配置id
     */
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
     * 准入限制
     */
    private Integer fileLimit;
    /**
     * 房间下注条件下限
     */
    private Integer roomBetLowLimit;
    /**
     * 台红值
     */
    private Long redValue;
    /**
     * 区域下注下限
     */
    private Long areaBetLowLimit;
    /**
     * 区域下注上限
     */
    private Long areaBetUpLimit;
    /**
     * 上庄下限
     */
    private Integer applyBankerLimit;
    /**
     * 自动下庄下限
     */
    private Integer bankerGoldLowLimit;
    /**
     * 连庄局数限制
     */
    private Integer bankRoundLimit;
    /**
     * 观战局数限制
     */
    private Integer watchRoundLimit;
    /**
     * 下注筹码
     */
    private String betChips;
}