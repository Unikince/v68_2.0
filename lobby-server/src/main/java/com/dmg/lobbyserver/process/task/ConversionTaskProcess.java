package com.dmg.lobbyserver.process.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.common.enums.ReceiveAwardStatusEnum;
import com.dmg.lobbyserver.dao.SysConvertibleConfigDao;
import com.dmg.lobbyserver.dao.TaskConfigDao;
import com.dmg.lobbyserver.dao.UserTaskProgressDao;
import com.dmg.lobbyserver.dao.bean.SysConvertibleConfigBean;
import com.dmg.lobbyserver.dao.bean.TaskConfigBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.dao.bean.UserTaskProgressBean;
import com.dmg.lobbyserver.model.dto.UserConversionDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.CONVERSION_TASK;

/**
 * @Description 积分兑换显示
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
@Service
@Slf4j
public class ConversionTaskProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return CONVERSION_TASK;
    }

    @Autowired
    UserService userService;
    @Autowired
    SysConvertibleConfigDao sysConvertibleConfigDao;
    @Autowired
    TaskConfigDao taskConfigDao;
    @Autowired
    UserTaskProgressDao userTaskProgressDao;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserConversionDTO UserConversionDTO = new UserConversionDTO();
        UserBean userById = userService.getUserById(Long.parseLong(userid));
        Long integral = userById.getIntegral();
        SysConvertibleConfigBean one = sysConvertibleConfigDao.getOne();
        UserConversionDTO.setIntegral(integral);
        UserConversionDTO.setProportion(one.getConvertibleProportion());
        //用戶进度
        Integer progress = 0;
        //总进度
        Integer allCondition = 0;
        List<TaskConfigBean> taskConfigBeans = taskConfigDao.selectList(new LambdaQueryWrapper<TaskConfigBean>().eq(TaskConfigBean::getTaskType, 4).
                eq(TaskConfigBean::getTaskStatus, 1));
        for (TaskConfigBean taskConfigBean : taskConfigBeans) {
            allCondition += taskConfigBean.getTaskCondition();
        }
        UserConversionDTO.setAllCondition(allCondition);
        UserTaskProgressBean userTaskProgressBeans = userTaskProgressDao.selectOne(new LambdaQueryWrapper<UserTaskProgressBean>()
                .eq(UserTaskProgressBean::getReceiveAwardStatus, ReceiveAwardStatusEnum.CODE_HAVE_RECEIVED.getCode())
                .eq(UserTaskProgressBean::getTaskType, 4)
                .eq(UserTaskProgressBean::getUserId, userid));
        if (userTaskProgressBeans != null) {
            progress = userTaskProgressBeans.getTaskProgress();
        } else {
            progress = 0;
        }
        UserConversionDTO.setUserCondition(progress);
        result.setMsg(JSON.toJSON(UserConversionDTO));
    }
}
