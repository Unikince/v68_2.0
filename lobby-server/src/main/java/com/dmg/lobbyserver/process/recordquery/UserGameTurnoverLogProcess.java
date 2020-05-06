package com.dmg.lobbyserver.process.recordquery;

import com.alibaba.fastjson.JSONObject;
import com.dmg.common.core.web.BaseResultEnum;
import com.dmg.common.core.web.Result;
import com.dmg.lobbyserver.model.dto.GamerecordDTO;
import com.dmg.lobbyserver.model.dto.UserGameRecordDTO;
import com.dmg.lobbyserver.model.vo.GameRecordVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.record.GameRecordService;
import com.dmg.server.common.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.GAME_RECORD;

/**
 * @Description 游戏记录
 * @Author jock
 * @Date 2019/6/19 0019
 * @Version V1.0
 **/
@Service
@Slf4j
public class UserGameTurnoverLogProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return GAME_RECORD;
    }

    @Autowired
    private GameRecordService gameRecordService;

    @Override
    public void messageHandler(String userId, JSONObject params, MessageResult result) {
        log.info("战绩查询req data:{}", params);
        GamerecordDTO gamerecordDTO = params.toJavaObject(GamerecordDTO.class);
        UserGameRecordDTO userGameRecordDTO = UserGameRecordDTO.builder()
                .userId(Long.parseLong(userId))
                .endTime(DateUtils.formatDate(DateUtils.getTomorrowZero(new Date(Long.parseLong(gamerecordDTO.getEndTime()))), "yyyy-MM-dd HH:mm:ss"))
                .gameId(gamerecordDTO.getType())
                .startTime(DateUtils.formatDate(new Date(Long.parseLong(gamerecordDTO.getStartTime())), "yyyy-MM-dd HH:mm:ss")).build();
        log.info("调用game-record-server服务查询战绩信息req:{}", userGameRecordDTO.toString());
        Result<List<GameRecordVO>> resultGameRecord;
        try {
            resultGameRecord = gameRecordService.getInfoList(userGameRecordDTO);
        } catch (Exception e) {
            log.error("调用game-record-server服务查询战绩信息出现异常:{}", e);
            return;
        }
        log.info("调用game-record-server服务查询战绩信息resp :{}", resultGameRecord.toString());
        if (!BaseResultEnum.SUCCESS.getCode().toString().equals(resultGameRecord.getCode())) {
            return;
        }
        if (resultGameRecord.getData() != null) {
            result.setMsg(resultGameRecord.getData());
        }
    }
}
