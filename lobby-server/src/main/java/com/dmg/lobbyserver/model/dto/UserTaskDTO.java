package com.dmg.lobbyserver.model.dto;

import com.dmg.lobbyserver.common.enums.ReceiveAwardStatusEnum;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Description
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
@Data
public class UserTaskDTO implements Comparable<UserTaskDTO>, Serializable {
    /**
     * 任务说明
     */
    private String taskDesc;
    /**
     * 任务条件描述
     */
    private String taskConditionDesc;
    /**
     * 用户是否领取
     */
    private Integer status = ReceiveAwardStatusEnum.CODE_NOT_COMPLETE.getCode();
    /**
     * 用户进度
     */
    private Integer condition = 0;
    /**
     * 任务进度
     */
    private Integer taskCondition;
    /**
     * 奖励
     */
    private List<TaskItemDetailsDTO> taskItemDetailsDTOS;
    /**
     * 任务iD
     */
    private Long taskId;
    /**
     * 任务类型
     */
    private Integer taskType;
    /**
     * 是否可跳转
     */
    private Boolean isJump = false;
    private Integer jumpId;
    private Integer pictureId;

    @Override
    public int compareTo(UserTaskDTO o) {
        Integer thisStatus = this.status;
        Integer otherStatus = o.status;
        if (thisStatus < otherStatus) {
            return -1;
        }
        if (thisStatus > otherStatus) {
            return 1;
        }
        return 0;
    }
}
