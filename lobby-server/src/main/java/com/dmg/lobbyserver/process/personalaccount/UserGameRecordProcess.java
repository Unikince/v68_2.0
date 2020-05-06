package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserGameRecordDao;
import com.dmg.lobbyserver.dao.bean.UserGameRecordBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.lobbyserver.config.MessageConfig.USER_RECORD;

/**
 * @Description 用户战绩
 * @Author jock
 * @Date 17:20
 * @Version V1.0
 **/
@Service
@Slf4j
public class UserGameRecordProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return USER_RECORD;
    }

    @Autowired
    UserGameRecordDao sysUserGameRecordDao;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Integer type = params.getInteger("type");
        List<UserGameRecordBean> sysUserGameRecordBeans = sysUserGameRecordDao.getRecord(Long.parseLong(userid),type);
        result.setMsg(sysUserGameRecordBeans);
    }
}
