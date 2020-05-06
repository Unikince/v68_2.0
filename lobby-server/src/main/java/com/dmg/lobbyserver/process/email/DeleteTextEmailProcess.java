package com.dmg.lobbyserver.process.email;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.EmailDao;
import com.dmg.lobbyserver.dao.UserEmailDao;
import com.dmg.lobbyserver.dao.bean.UserEmailBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.DELETE_TEXT_EMAIL;

/**
 * @Description 删除站内信息
 * @Author mice
 * @Date 2019/6/20 11:04
 * @Version V1.0
 **/
@Service
public class DeleteTextEmailProcess implements AbstractMessageHandler {

    @Autowired
    private UserEmailDao userEmailDao;

    @Override
    public String getMessageId() {
        return DELETE_TEXT_EMAIL;
    }

    @Override
    public void messageHandler(String userId, JSONObject params, MessageResult result) {
        Long emailId = params.getLong("emailId");
        if (emailId == null || emailId < 1) {
            result.setRes(ResultEnum.PARAM_ERROR.getCode());
            return;
        }
        userEmailDao.delete(new LambdaQueryWrapper<UserEmailBean>()
                .eq(UserEmailBean::getUserId, Long.parseLong(userId))
                .eq(UserEmailBean::getId, emailId));
    }
}