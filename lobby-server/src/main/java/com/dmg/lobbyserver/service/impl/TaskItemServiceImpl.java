package com.dmg.lobbyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDetailDao;
import com.dmg.lobbyserver.dao.TaskConfigDao;
import com.dmg.lobbyserver.dao.bean.*;
import com.dmg.lobbyserver.model.dto.TaskConfigDTO;
import com.dmg.lobbyserver.model.dto.TaskItemDTO;
import com.dmg.lobbyserver.model.dto.TaskItemDetailsDTO;
import com.dmg.lobbyserver.service.TaskItemService;
import com.dmg.server.common.util.DmgBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description 任务奖励详情
 * @Author jock
 * @Date 2019/7/6 0006
 * @Version V1.0
 **/
@Service
@Slf4j
public class TaskItemServiceImpl implements TaskItemService {

    @Autowired
    private TaskConfigDao taskConfigDao;

    @Autowired
    private SysRewardConfigDetailDao sysRewardConfigDetailDao;

    @Autowired
    private SysRewardConfigDao sysRewardConfigDao;

    @Autowired
    private SysItemConfigDao sysItemConfigDao;

    @Override
    public List<TaskItemDTO> getTaskItemDetails(Integer type) {
        List<TaskConfigBean> taskConfigBeans = taskConfigDao.selectList(new LambdaQueryWrapper<TaskConfigBean>()
                .eq(TaskConfigBean::getTaskClassification, type)
                .eq(TaskConfigBean::getTaskStatus, 1)
                .and(i -> i.isNotNull(TaskConfigBean::getTaskStartTime)
                        .le(TaskConfigBean::getTaskStartTime, new Date())
                        .or().isNull(TaskConfigBean::getTaskStartTime))
                .and(i -> i.isNotNull(TaskConfigBean::getTaskEndTime)
                        .gt(TaskConfigBean::getTaskEndTime, new Date())
                        .or().isNull(TaskConfigBean::getTaskEndTime))
                .orderByAsc(TaskConfigBean::getTaskCondition));
        //发送数据(奖励 ,任务配置等)
        List<TaskItemDTO> taskItemDTOS = new ArrayList<>();
        //装在奖励物品
        taskConfigBeans.forEach(taskConfigBean -> {
            TaskItemDTO taskItemDTO = new TaskItemDTO();
            taskItemDTO.setTaskId(taskConfigBean.getId());

            TaskConfigDTO taskConfigDTO = new TaskConfigDTO();
            DmgBeanUtils.copyProperties(taskConfigBean, taskConfigDTO);
            taskItemDTO.setTaskConfigDTO(taskConfigDTO);

            List<SysRewardConfigBean> rewards = sysRewardConfigDao.getRewards(taskConfigBean.getTaskRewardIds());
            //系统奖励配置详情
            List<TaskItemDetailsDTO> sysItemConfigBeans = new ArrayList<>();
            rewards.forEach(reward -> {
                List<SysRewardConfigDetailBean> sysRewards = sysRewardConfigDetailDao.getSysRewards(reward.getRewardDetailId());
                for (SysRewardConfigDetailBean sysRewardConfigDetailBean : sysRewards) {
                    //任务奖励
                    TaskItemDetailsDTO taskItemDetailsDTO = new TaskItemDetailsDTO();
                    SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectOne(new LambdaQueryWrapper<SysItemConfigBean>().
                            eq(SysItemConfigBean::getId, sysRewardConfigDetailBean.getItemId()));

                    DmgBeanUtils.copyProperties(sysItemConfigBean, taskItemDetailsDTO);
                    taskItemDetailsDTO.setNum(sysRewardConfigDetailBean.getItemNum());
                    taskItemDetailsDTO.setItemDesc(sysItemConfigBean.getRemark());
                    sysItemConfigBeans.add(taskItemDetailsDTO);
                }
            });
            taskItemDTO.setTaskItemDetailsDTOS(sysItemConfigBeans);
            taskItemDTOS.add(taskItemDTO);
        });
        return taskItemDTOS;
    }
}
