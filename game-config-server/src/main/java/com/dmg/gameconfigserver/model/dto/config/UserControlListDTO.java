package com.dmg.gameconfigserver.model.dto.config;

import java.math.BigDecimal;
import java.util.Date;

import com.dmg.gameconfigserver.annotation.ColumnName;
import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;

/**
 * 用户控制列表
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
@ColumnName(name = "用户控制列表")
public class UserControlListDTO  extends PageReqDTO {
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
    private BigDecimal totalWinLose = BigDecimal.ZERO;
    /**
     * 总下注
     */
    private BigDecimal totalBet = BigDecimal.ZERO;
    /**
     * 总赔付
     */
    
    private BigDecimal totalPayout = BigDecimal.ZERO;
    /**
     * 控制状态
     */
    private Integer controlState;
    /**
     * 控制分数
     */
    @ColumnName(name = "控制分数")
    private BigDecimal controlScore;
    /**
     * 控制模型
     */
    @ColumnName(name = "控制模型")
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
    private Date operatingTime;
    /**
     * 操作备注
     */
    @ColumnName(name = "操作备注")
    private String operatingNote;
}