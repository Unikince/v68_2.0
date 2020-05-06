package com.dmg.gameconfigserver.model.bean.config;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户控制列表
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
@TableName("t_user_control_list")
public class UserControlListBean {
	@TableId(type = IdType.AUTO)
    private Integer id;
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
    //private BigDecimal totalWinLose;
    /**
     * 总下注
     */
   //private BigDecimal totalBet;
    /**
     * 总赔付
     */
    //private BigDecimal totalPayout;
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
    private Date operatingTime;
    /**
     * 操作备注
     */
    private String operatingNote;
}