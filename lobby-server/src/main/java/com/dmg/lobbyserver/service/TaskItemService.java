package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.model.dto.TaskItemDTO;

import java.util.List;

/**
 * @Description 获取任务奖励
 * @Author jock
 * @Date 2019/7/6 0006
 * @Version V1.0
 **/

public interface TaskItemService {

    List<TaskItemDTO> getTaskItemDetails(Integer type);

}
