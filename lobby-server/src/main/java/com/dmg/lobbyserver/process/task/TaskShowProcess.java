package com.dmg.lobbyserver.process.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.dao.*;
import com.dmg.lobbyserver.dao.bean.*;
import com.dmg.lobbyserver.model.dto.TaskItemDTO;
import com.dmg.lobbyserver.model.dto.UserTaskDTO;
import com.dmg.lobbyserver.model.vo.UserTaskVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.TaskItemService;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.server.common.enums.TaskTypeEnum;
import com.dmg.server.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.DAILY_TASK;

/**
 * @Description 显示任务
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
@Slf4j
@Service
public class TaskShowProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return DAILY_TASK;
    }

    @Autowired
    private UserTaskProgressDao userTaskProgressDao;

    @Autowired
    private TaskItemService taskItemService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void messageHandler(String userId, JSONObject params, MessageResult result) {
        //类型
        Integer type = params.getInteger("type");
        List<TaskItemDTO> taskItemDetails = taskItemService.getTaskItemDetails(type);
        //用户任务
        List<UserTaskProgressBean> userTaskProgressBeans = userTaskProgressDao.selectList(new LambdaQueryWrapper<UserTaskProgressBean>().
                eq(UserTaskProgressBean::getUserId, userId).
                eq(UserTaskProgressBean::getTaskType, type));
        //推送消息
        UserTaskDTO userTaskDTO;
        List<UserTaskDTO> userTaskDTOList = new ArrayList<>();
        List<UserTaskDTO> activityLevelTask = new ArrayList<>();
        for (TaskItemDTO taskItemDetail : taskItemDetails) {
            userTaskDTO = new UserTaskDTO();
            userTaskDTO.setTaskItemDetailsDTOS(taskItemDetail.getTaskItemDetailsDTOS());
            userTaskDTO.setTaskCondition(taskItemDetail.getTaskConfigDTO().getTaskCondition());
            userTaskDTO.setTaskConditionDesc(taskItemDetail.getTaskConfigDTO().getTaskConditionDesc());
            userTaskDTO.setTaskDesc(taskItemDetail.getTaskConfigDTO().getTaskDesc());
            userTaskDTO.setTaskId(taskItemDetail.getTaskConfigDTO().getId());
            if (taskItemDetail.getTaskConfigDTO().getJumpId() != null) {
                userTaskDTO.setJumpId(taskItemDetail.getTaskConfigDTO().getJumpId());
                userTaskDTO.setIsJump(true);
            }
            userTaskDTO.setTaskType(taskItemDetail.getTaskConfigDTO().getTaskType());
            userTaskDTO.setPictureId(taskItemDetail.getTaskConfigDTO().getPictureId());
            if (userTaskProgressBeans.size() <= 0) {
                userTaskDTOList.add(userTaskDTO);
                continue;
            }
            for (UserTaskProgressBean userTaskProgressBean : userTaskProgressBeans) {
                if (!taskItemDetail.getTaskId().equals(userTaskProgressBean.getTaskId())) {
                    continue;
                }
                userTaskDTO.setCondition(userTaskProgressBean.getTaskProgress());
                if (userTaskProgressBean.getReceiveAwardStatus() == 2) {
                    userTaskDTO.setStatus(2);
                } else if ((userTaskProgressBean.getReceiveAwardStatus() != 2) && taskItemDetail.getTaskConfigDTO().getTaskCondition() <= userTaskProgressBean.getTaskProgress()) {
                    userTaskDTO.setStatus(0);
                }
                break;
            }
            if (userTaskDTO.getTaskType() == TaskTypeEnum.CODE_ACTIVITYLEVEL.getCode()) {
                activityLevelTask.add(userTaskDTO);
            } else {
                userTaskDTOList.add(userTaskDTO);
            }
        }
        Collections.sort(userTaskDTOList);
        Object object = redisUtil.get(RedisKey.USER_ACTIVITY_LEVEL.concat(":").concat(DateUtils.getDate("yyyyMMdd"))
                .concat(":").concat(userId));
        result.setMsg(JSON.toJSON(UserTaskVO.builder()
                .userId(Long.parseLong(userId))
                .activityLevel(object != null ? Double.valueOf(String.valueOf(object)) : 0)
                .userTask(userTaskDTOList)
                .activityLevelTask(activityLevelTask).build()));
    }
}
