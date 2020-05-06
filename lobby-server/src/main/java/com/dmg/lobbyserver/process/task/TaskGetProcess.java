package com.dmg.lobbyserver.process.task;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.model.dto.CommonRespDTO;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.lobbyserver.service.PushService;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.GET_TASK;

/**
 * @Description 领取任务
 * @Author jock
 * @Date 2019/6/24 0024
 * @Version V1.0
 **/
@Service
@Slf4j
public class TaskGetProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return GET_TASK;
    }

    @Autowired
    private UserTaskProgressService userTaskProgressService;

    @Autowired
    private GiftDataService giftDataService;

    @Autowired
    private PushService pushService;

    @Override
    public void messageHandler(String userId, JSONObject params, MessageResult result) {
        Long taskId = params.getLong("taskId");
        CommonRespDTO<List<GiftDataDTO>> giftDataDTOList = userTaskProgressService.userTaskReceive(Long.parseLong(userId), taskId, null);
        if (!giftDataDTOList.getStatus()) {
            pushService.push(Long.parseLong(userId), GET_TASK, giftDataDTOList.getCode() != null ? giftDataDTOList.getCode() : ResultEnum.TASK_RECEIVE.getCode());
        } else {
            giftDataService.sendGiftData(Long.parseLong(userId), giftDataDTOList.getData());
        }
    }
}
