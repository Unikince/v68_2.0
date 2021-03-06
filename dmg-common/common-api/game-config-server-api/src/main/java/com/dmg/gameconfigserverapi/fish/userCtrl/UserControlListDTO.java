package com.dmg.gameconfigserverapi.fish.userCtrl;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 用户控制记录
 **/
@Data
public class UserControlListDTO {
    /**
     * 玩家id
     */
    private Long userId;
    /**
     * 玩家昵称
     */
    private String userNickname;
    /**
     * 总输赢
     */
    private BigDecimal totalWinLose;
    /**
     * 总下注
     */
    private BigDecimal totalBet;
    /**
     * 总赔付
     */
    private BigDecimal totalPayout;
    /**
     * 控制状态
     */
    private Integer controlState;
    /**
     * 控制分数
     */
    private BigDecimal controlScore;
    /**
     * 控制模型
     */
    private Integer controlModel;
    /**
     * 当前分数
     */
    private BigDecimal currentScore;
    /**
     * 操作人员
     */
    private String operator;
    /**
     * 操作时间
     */
    private String operatingTime;
    /**
     * 操作备注
     */
    private String operatingNote;
}