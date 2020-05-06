package com.dmg.game.record.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:20 2019/11/26
 */
@Data
public class TaskConfigDTO {
    private Long id;
    /**
     * 任务类型 1  每日任务 2 存款任务 3 游戏任务 4:积分兑换  5活动
     */
    private Integer taskType;
    /**
     * 任务描述
     */
    private String taskDesc;
    /**
     * 任务条件
     */
    private Integer taskCondition;
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
