package com.dmg.lobbyserver.process.email;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserEmailDao;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.HAS_RED_EMAIL;

/**
 * @Description 标记已读邮件
 * @Author mice
 * @Date 2019/6/20 11:04
 * @Version V1.0
 **/
@Service
public class ReadEmailProcess implements AbstractMessageHandler {

    @Autowired
    private UserEmailDao userEmailDao;

    @Override
    public String getMessageId() {
        return HAS_RED_EMAIL;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Long emailId = params.getLong("emailId");
        if (emailId == null || emailId < 1) {
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        userEmailDao.updateHasRead(emailId);
    }
}