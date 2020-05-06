package com.zyhy.lhj_server.bgmanagement.feign.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 用户控制记录
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
public class UserControlListVO {
    /**
     * 玩家id
     */
	@NotNull(message = "userId不能为空")
    private Integer userId;
    /**
     * 玩家昵称
     */
    private String userNickname;
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
    private String operatingTime;
    /**
     * 操作备注
     */
    private String operatingNote;
}