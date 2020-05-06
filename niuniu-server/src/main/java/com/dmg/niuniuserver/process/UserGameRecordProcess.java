package com.dmg.niuniuserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.dao.UserGameRecordDao;
import com.dmg.niuniuserver.dao.bean.UserGameRecordBean;
import com.dmg.niuniuserver.result.MessageResult;
import com.dmg.niuniuserver.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dmg.niuniuserver.config.MessageConfig.USER_RECORD;


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
    private PushService pushService;

    @Autowired
    UserGameRecordDao sysUserGameRecordDao;

    @Override
    public void messageHandler(Long userid, JSONObject params) {
        Integer type = params.getInteger("type");
        List<UserGameRecordBean> sysUserGameRecordBeans = sysUserGameRecordDao.getRecord(userid,type);
        MessageResult result = new MessageResult(USER_RECORD,sysUserGameRecordBeans);
        result.setMsg(sysUserGameRecordBeans);
        pushService.push(userid,result);
    }
}
