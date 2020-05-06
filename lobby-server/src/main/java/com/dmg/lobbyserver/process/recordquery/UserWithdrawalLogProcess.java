package com.dmg.lobbyserver.process.recordquery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserWithdrawalLogDao;
import com.dmg.lobbyserver.dao.bean.UserWithdrawalLogBean;
import com.dmg.lobbyserver.model.dto.GamerecordDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.DRAWINGS_RECORD;

/**
 * @Description 提款纪录
 * @Author jock
 * @Date 2019/6/19 0019
 * @Version V1.0
 **/
@Service
@Slf4j
public class UserWithdrawalLogProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return DRAWINGS_RECORD;
    }

    @Autowired
    UserWithdrawalLogDao userWithdrawalLogDao;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        GamerecordDTO gamerecordDTO = params.toJavaObject(GamerecordDTO.class);
        List<UserWithdrawalLogBean> gamerecord = userWithdrawalLogDao.getUserWithdrawal(gamerecordDTO.getType(),
                gamerecordDTO.getStartTime(), gamerecordDTO.getEndTime(),userid);
            result.setMsg(JSON.toJSON(gamerecord));
    }
}
