package com.dmg.game.record.service.consumer;

import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.web.Result;
import com.dmg.common.starter.rocketmq.annotation.RocketMQMessageListener;
import com.dmg.common.starter.rocketmq.core.listener.RocketMQListener;
import com.dmg.game.record.model.GameRecordBean;
import com.dmg.game.record.model.dto.UserTaskChangeReqDTO;
import com.dmg.game.record.model.dto.UserTaskProgressDTO;
import com.dmg.game.record.service.GameRecordService;
import com.dmg.game.record.service.feign.UserControlService;
import com.dmg.game.record.service.task.TaskService;
import com.dmg.server.common.enums.TaskTypeEnum;
import com.dmg.server.common.model.dto.GameRecordDTO;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:20 2019/12/27
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "GAME_RECORD_CONSUMER", topic = "GAME_RECORD", tags = "GAME_RECORD")
public class RecordListener implements RocketMQListener {

    @Autowired
    private GameRecordService gameRecordService;
    
    @Autowired
    private UserControlService userControlService;
    
    @Autowired
    private TaskService taskService;

    @Override
    public void onMessage(MessageExt msg) {
        String data = null;
        try {
            data = new String(msg.getBody(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("消息解析异常{}", e);
            return;
        }
        if (StringUtils.isBlank(data)) {
            log.info("接收到的消息为空，不做任何处理");
            return;
        }
        this.saveData(data);
    }

    /**
     * @Author liubo
     * @Description //TODO
     * @Date 13:41 2019/11/19
     **/
    private void saveData(String body) {
        GameRecordBean gameRecordBean = JSONObject.parseObject(body, GameRecordBean.class);
        if (!gameRecordService.insertGameRecord(gameRecordBean) || gameRecordBean.getIsRobot()) {
            return;
        }
        List<UserTaskChangeReqDTO> req = new ArrayList<>();
        req.add(UserTaskChangeReqDTO.builder()
                .userId(gameRecordBean.getUserId())
                .taskType(TaskTypeEnum.CODE_GAME.getCode())
                .number(1).build());
        if (gameRecordBean.getWinLosGold().compareTo(BigDecimal.ZERO) > 0) {
            req.add(UserTaskChangeReqDTO.builder()
                    .userId(gameRecordBean.getUserId())
                    .taskType(TaskTypeEnum.CODE_GAME_WIN.getCode())
                    .number(1).build());
            req.add(UserTaskChangeReqDTO.builder()
                    .userId(gameRecordBean.getUserId())
                    .taskType(TaskTypeEnum.CODE_GAME_WIN_GOLD.getCode())
                    .number(gameRecordBean.getWinLosGold().intValue()).build());
        }
        try {
            log.info("调用lobby服务【变更任务进度】req：{}", req.toString());
            Result<List<UserTaskProgressDTO>> result = taskService.userTaskChange(req);
            log.info("调用lobby服务【变更任务进度】resp：{}", result.toString());
        } catch (Exception e) {
            log.error("任务进度变更出现异常:{}", e);
        }

        try{
        	@SuppressWarnings("rawtypes")
			GameRecordDTO dto = JSONObject.parseObject(body, GameRecordDTO.class);
        	boolean flag = dto.isControlState();
        	if(flag && !dto.getIsRobot()){
        		Long userId = dto.getUserId();
        		BigDecimal winLosGold=dto.getWinLosGold();
        		userControlService.updateCurrentScore(userId, winLosGold);
        	}
        }catch(Exception e){
        	log.error("",e);
        }
        
    }
}
