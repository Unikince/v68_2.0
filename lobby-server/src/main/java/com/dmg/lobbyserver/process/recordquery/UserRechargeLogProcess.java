package com.dmg.lobbyserver.process.recordquery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserRechargeLogDao;
import com.dmg.lobbyserver.dao.bean.UserRechargeLogBean;
import com.dmg.lobbyserver.dao.bean.VnnnRechargeLogBean;
import com.dmg.lobbyserver.model.dto.GamerecordDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.VOUCHER_RECORD;

/**
 * @Description 充值记录
 * @Author jock
 * @Date 2019/6/19 0019
 * @Version V1.0
 **/
@Slf4j
@Service
public class UserRechargeLogProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return VOUCHER_RECORD;
    }

    @Autowired
    UserRechargeLogDao userRechargeLogDao;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        GamerecordDTO gamerecordDTO = params.toJavaObject(GamerecordDTO.class);
        List<UserRechargeLogBean> gamerecord = userRechargeLogDao.getUserRechargeLog(
                gamerecordDTO.getStartTime(), gamerecordDTO.getEndTime(), gamerecordDTO.getType(),userid);
            result.setMsg(JSON.toJSON(gamerecord));
    }
}
