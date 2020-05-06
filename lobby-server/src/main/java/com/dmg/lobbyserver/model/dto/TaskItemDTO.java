package com.dmg.lobbyserver.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description 任务奖励
 * @Author JOCK
 * @Date 2019/7/6 0006
 * @Version V1.0
 **/
@Data
public class TaskItemDTO {
    /**
     * 奖励,奖励物品详情
     */
    private List<TaskItemDetailsDTO> taskItemDetailsDTOS;
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 任务配置详情
     */
    private TaskConfigDTO taskConfigDTO;
}
