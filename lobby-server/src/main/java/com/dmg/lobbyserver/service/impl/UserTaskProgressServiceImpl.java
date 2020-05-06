package com.dmg.lobbyserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.server.common.enums.ItemTypeEnum;
import com.dmg.lobbyserver.common.enums.ReceiveAwardStatusEnum;
import com.dmg.lobbyserver.common.enums.TaskClassificationEnum;
import com.dmg.lobbyserver.dao.*;
import com.dmg.lobbyserver.dao.bean.*;
import com.dmg.lobbyserver.model.dto.CommonRespDTO;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.model.vo.SyncUserGoldVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import com.dmg.server.common.enums.AccountChangeTypeEnum;
import com.dmg.server.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/20 10:39
 * @Version V1.0
 **/
@Slf4j
@Service
public class UserTaskProgressServiceImpl implements UserTaskProgressService {

    @Autowired
    private UserTaskProgressDao userTaskProgressDao;

    @Autowired
    private TaskConfigDao taskConfigDao;

    @Autowired
    private SysRewardConfigDao sysRewardConfigDao;

    @Autowired
    private SysRewardConfigDetailDao sysRewardConfigDetailDao;

    @Autowired
    private SysItemConfigDao sysItemConfigDao;

    @Autowired
    private UserService userService;

    @Autowired
    private AbstractMessageHandler syncUserGoldProcess;


    @Override
    public boolean getHasFinishAndNoRecieveAwardTask(Long userId) {
        if (userId == null || userId <= 0) {
            return false;
        }
        int has = userTaskProgressDao.countHasFinishAndNoRecieveAwardTasks(userId);
        if (has > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void userTaskChange(Long userId, Integer taskType, Integer number, Integer maxNumber) {
        List<TaskConfigBean> taskConfigBeanList = taskConfigDao.selectList(new LambdaQueryWrapper<TaskConfigBean>()
                .in(TaskConfigBean::getTaskType, taskType)
                .eq(TaskConfigBean::getTaskStatus, 1)
                .and(i -> i.isNotNull(TaskConfigBean::getTaskStartTime)
                        .le(TaskConfigBean::getTaskStartTime, new Date())
                        .or().isNull(TaskConfigBean::getTaskStartTime))
                .and(i -> i.isNotNull(TaskConfigBean::getTaskEndTime)
                        .gt(TaskConfigBean::getTaskEndTime, new Date())
                        .or().isNull(TaskConfigBean::getTaskEndTime)));
        if (taskConfigBeanList == null || taskConfigBeanList.size() < 0) {
            return;
        }
        taskConfigBeanList.forEach(taskConfigBean -> {
            LambdaQueryWrapper<UserTaskProgressBean> queryWrapper = new LambdaQueryWrapper<UserTaskProgressBean>()
                    .eq(UserTaskProgressBean::getUserId, userId)
                    .eq(UserTaskProgressBean::getTaskId, taskConfigBean.getId());
            //每日任务处理
            if (taskConfigBean.getTaskClassification() == TaskClassificationEnum.CODE_EVERYDAY.getCode()) {
                queryWrapper.ge(UserTaskProgressBean::getCreateDate, DateUtils.getZero())
                        .lt(UserTaskProgressBean::getCreateDate, DateUtils.getTomorrowZero());
            }
            List<UserTaskProgressBean> userTaskProgressBeanList = userTaskProgressDao.selectList(queryWrapper);
            if (userTaskProgressBeanList == null || userTaskProgressBeanList.size() < 1) {
                Integer taskProgress = maxNumber != null ? maxNumber : number;
                userTaskProgressDao.insert(UserTaskProgressBean.builder()
                        .createDate(new Date())
                        .taskId(taskConfigBean.getId())
                        .userId(userId)
                        .taskProgress(taskProgress > taskConfigBean.getTaskCondition() ? taskConfigBean.getTaskCondition() : taskProgress)
                        .taskType(taskConfigBean.getTaskClassification()).build());
            } else {
                UserTaskProgressBean userTaskProgressBean = userTaskProgressBeanList.get(0);
                if (userTaskProgressBean.getReceiveAwardStatus() == ReceiveAwardStatusEnum.CODE_HAVE_RECEIVED.getCode()
                        || userTaskProgressBean.getTaskProgress() >= taskConfigBean.getTaskCondition()) {
                    return;
                }
                userTaskProgressDao.updateTaskProgress(userTaskProgressBean.getId(), number, taskConfigBean.getTaskCondition(), maxNumber);
            }
        });
    }

    @Override
    public CommonRespDTO<List<GiftDataDTO>> userTaskReceive(Long userId, Long taskId, String taskType) {
        //用户未领取的任务
        LambdaQueryWrapper<UserTaskProgressBean> queryWrapper = new LambdaQueryWrapper<UserTaskProgressBean>()
                .eq(UserTaskProgressBean::getUserId, userId)
                .in(UserTaskProgressBean::getReceiveAwardStatus, new Integer[]{ReceiveAwardStatusEnum.CODE_SURE_RECEIVE.getCode(), ReceiveAwardStatusEnum.CODE_NOT_COMPLETE.getCode()});
        if (StringUtils.isNotEmpty(taskType)) {
            queryWrapper.eq(UserTaskProgressBean::getTaskType, taskType);
        }
        if (taskId != null) {
            queryWrapper.eq(UserTaskProgressBean::getTaskId, taskId);
        }
        List<UserTaskProgressBean> userTaskProgressBeanList = userTaskProgressDao.selectList(queryWrapper);
        if (userTaskProgressBeanList == null || userTaskProgressBeanList.size() < 1) {
            return CommonRespDTO.<List<GiftDataDTO>>builder().status(false).code(ResultEnum.TASK_NOT_REWARD.getCode()).build();
        }
        List<GiftDataDTO> giftDataDTOList = new ArrayList<>();
        userTaskProgressBeanList.forEach(userTaskProgressBean -> {
            //得到	任务奖励对应奖励表中的ID(一个任务有多个奖励用逗号分隔)
            TaskConfigBean taskConfigBean = taskConfigDao.selectOne(new LambdaQueryWrapper<TaskConfigBean>()
                    .eq(TaskConfigBean::getId, userTaskProgressBean.getTaskId()));

            if (taskConfigBean.getTaskCondition() > userTaskProgressBean.getTaskProgress()) {
                return;
            }
            String[] rewardIds = taskConfigBean.getTaskRewardIds().split(",");
            log.info("用户:{}获得礼包id:{}", userId, JSONObject.toJSONString(rewardIds));
            List<SysRewardConfigBean> sysRewardConfigBeanArrayList = sysRewardConfigDao.selectList(new LambdaQueryWrapper<SysRewardConfigBean>()
                    .in(SysRewardConfigBean::getId, rewardIds));

            List<String> rewardDetailIds = new ArrayList<>();
            sysRewardConfigBeanArrayList.forEach(sysRewardConfigBean -> {
                StringTokenizer st = new StringTokenizer(sysRewardConfigBean.getRewardDetailId(), ",");
                while (st.hasMoreTokens()) {
                    rewardDetailIds.add(st.nextToken());
                }
            });
            List<SysRewardConfigDetailBean> sysRewardConfigDetailBeanList = sysRewardConfigDetailDao.selectList(new LambdaQueryWrapper<SysRewardConfigDetailBean>()
                    .in(SysRewardConfigDetailBean::getId, rewardDetailIds));

            BigDecimal changeAccount = BigDecimal.ZERO;
            Long integral = Long.parseLong("0");
            Double activityLevel = Double.parseDouble("0");
            for (SysRewardConfigDetailBean sysRewardConfigDetailBean : sysRewardConfigDetailBeanList) {
                GiftDataDTO giftDataDTO = new GiftDataDTO();
                SysItemConfigBean sysItemConfigBean = sysItemConfigDao.selectOne(new LambdaQueryWrapper<SysItemConfigBean>().
                        eq(SysItemConfigBean::getId, sysRewardConfigDetailBean.getItemId()));
                log.info("用户:{}获得奖品:{}:{}", userId, sysItemConfigBean.getId(), sysItemConfigBean.getItemName());
                giftDataDTO.setItemName(sysItemConfigBean.getItemName());
                giftDataDTO.setItemNumber(String.valueOf(sysRewardConfigDetailBean.getItemNum()));
                giftDataDTO.setRemark(sysItemConfigBean.getRemark());
                giftDataDTO.setSmallPicId(sysItemConfigBean.getSmallPicId());
                giftDataDTOList.add(giftDataDTO);
                // 1:金币 2:积分
                if (sysItemConfigBean.getItemType() == ItemTypeEnum.CODE_GOLD.getCode()) {
                    changeAccount = changeAccount.add(BigDecimal.valueOf(sysRewardConfigDetailBean.getItemNum()));
                } else if (sysItemConfigBean.getItemType() == ItemTypeEnum.CODE_INTEGRAL.getCode()) {
                    integral = integral + sysRewardConfigDetailBean.getItemNum();
                } else if (sysItemConfigBean.getItemType() == ItemTypeEnum.CODE_ACTIVITY_LEVEL.getCode()) {
                    activityLevel = activityLevel + sysRewardConfigDetailBean.getItemNum();
                }
            }
            if (changeAccount.compareTo(BigDecimal.ZERO) != 0) {
                SyncUserGoldVO syncUserGoldVO = new SyncUserGoldVO();
                syncUserGoldVO.setGold(changeAccount);
                syncUserGoldVO.setUserId(userId);
                syncUserGoldVO.setType(AccountChangeTypeEnum.CODE_TASK.getCode());
                syncUserGoldProcess.messageHandler(String.valueOf(userId), (JSONObject) JSONObject.toJSON(syncUserGoldVO), null);
            }
            if (integral != 0) {
                userService.changeIntegral(userId, integral);
            }
            if (activityLevel != 0) {
                userService.changeActivityLevel(userId, activityLevel);
            }
            userTaskProgressBean.setReceiveAwardStatus(ReceiveAwardStatusEnum.CODE_HAVE_RECEIVED.getCode());
            //更新领取状态
            userTaskProgressDao.updateById(userTaskProgressBean);
        });
        log.info("用户:{}获得奖品:{}", userId, giftDataDTOList.toString());
        return CommonRespDTO.<List<GiftDataDTO>>builder()
                .data(giftDataDTOList)
                .status(giftDataDTOList.size() < 1 ? false : true).build();
    }
}