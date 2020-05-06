package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.model.dto.CommonRespDTO;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;

import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/20 10:01
 * @Version V1.0
 **/
public interface UserTaskProgressService {

    /**
     * @param userId
     * @return boolean
     * @description: 玩家是否有已完成且未领取奖励的任务
     * @author mice
     * @date 2019/6/20
     */
    boolean getHasFinishAndNoRecieveAwardTask(Long userId);

    /**
     * @Author liubo
     * @Description //TODO 任务进度变更
     * @Date 13:54 2019/11/26
     **/
    void userTaskChange(Long userId, Integer taskType, Integer number, Integer maxNumber);

    /**
     * @Author liubo
     * @Description //TODO 任务领取
     * @Date 10:28 2019/11/27
     **/
    CommonRespDTO<List<GiftDataDTO>> userTaskReceive(Long userId, Long taskId, String taskType);
}