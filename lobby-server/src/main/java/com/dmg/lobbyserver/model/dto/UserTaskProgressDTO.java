package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:44 2019/11/26
 */
@Data
public class UserTaskProgressDTO {

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务进度
     */
    private Integer taskProgress;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 任务类型 1  每日任务 2 每周任务 3 成长任务
     */
    private Integer taskType;
    /**
     * 是否领取奖励(0:未领取 1;已领取)
     */
    private Integer receiveAwardStatus;
}
