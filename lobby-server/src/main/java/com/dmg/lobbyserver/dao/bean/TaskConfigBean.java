package com.dmg.lobbyserver.dao.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务配置表
 *
 * @author mice
 * @email .com
 * @date 2019-06-17 18:38:38
 */
@Data
@TableName("task_config")
public class TaskConfigBean implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 任务类型 1 游戏 2 分享 3 登陆 4 购买商品 5 金币突破
     */
    private Integer taskType;
    /**
     * 任务分类：1：每日任务 2：成长任务
     */
    private Integer taskClassification;
    /**
     * 任务描述
     */
    private String taskDesc;
    /**
     * 任务条件
     */
    private Integer taskCondition;
    /**
     * 任务条件描述
     */
    private String taskConditionDesc;
    /**
     * 任务奖励对应奖励表中的ID(一个任务有多个奖励用逗号分隔)
     */
    private String taskRewardIds;
    /**
     * 任务状态(0:关闭 1:开启)
     */
    private Integer taskStatus;
    /**
     * 任务开始时间
     */
    private Date taskStartTime;
    /**
     * 任务结束时间
     */
    private Date taskEndTime;
    /**
     * 1:每日清除 2:每周清除 3:每月清除
     */
    private Integer clearType;
    /**
     * 跳转id
     */
    private Integer jumpId;
    /**
     * 跳转 图片id
     */
    private Integer pictureId;

}
