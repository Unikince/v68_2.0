package com.dmg.gameconfigserver.model.bean.config;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * 用户控制记录
 * @Description
 * @Author mice
 * @Date 2019/9/26 19:46
 * @Version V1.0
 **/
@Data
@TableName("t_user_control_record")
public class UserControlRecordBean {
	@TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 玩家id
     */
    private Long userId;
    /**
     * 控制类型
     */
    private Integer controlType;
    /**
     * 玩家输赢(被控制时的输赢)
     */
    private BigDecimal winLose;
    /**
     * 控制分数
     */
    private BigDecimal controlScore;
    /**
     * 控制模型
     */
    private Integer controlModel;
    /**
     * 控制结束时分数
     */
    private BigDecimal controlEndScore;
    /**
     * 控制开始时间
     */
    private Date controlStartTime;
    /**
     * 控制结束时间
     */
    private Date controlEndTime;
    /**
     * 操作人员
     */
    private String operator;
    /**
     * 操作备注
     */
    private String operatingNote;
}