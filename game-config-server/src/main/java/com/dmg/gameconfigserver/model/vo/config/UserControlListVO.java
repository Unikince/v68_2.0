package com.dmg.gameconfigserver.model.vo.config;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.dmg.gameconfigserver.model.dto.PageReqDTO;

import lombok.Data;

/**
 * 用户控制记录
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
public class UserControlListVO extends PageReqDTO {
    /**
     * 玩家id
     */
	@NotNull(message = "userId不能为空")
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
	@NotNull(message = "controlScore不能为空")
    private BigDecimal controlScore;
    /**
     * 控制模型
     */
	@NotNull(message = "controlModel不能为空")
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
    private String operatingNote;
}