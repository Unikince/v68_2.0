package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserSmsConfigDao;
import com.dmg.lobbyserver.dao.bean.UserSmsConfigBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.GET_USER_SMS_CONFIG;

/**
 * @Description 获取用户短信配置
 * @Author mice
 * @Date 2019/6/21 17:40
 * @Version V1.0
 **/
@Service
public class UserSmsConfigProcess implements AbstractMessageHandler {
    @Autowired
    private UserSmsConfigDao userSmsConfigDao;
    @Override
    public String getMessageId() {
        return GET_USER_SMS_CONFIG;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserSmsConfigBean userSmsConfigBean = userSmsConfigDao.selectOne(new LambdaQueryWrapper<UserSmsConfigBean>().eq(UserSmsConfigBean::getUserId,userid));
        if (userSmsConfigBean == null){
            userSmsConfigBean = new UserSmsConfigBean();
            userSmsConfigBean.setUserId(Long.parseLong(userid));
            userSmsConfigBean.setChangeBankCard(0);
            userSmsConfigBean.setWithdrawalMessage(0);
            userSmsConfigBean.setChangeBankCard(0);
            userSmsConfigBean.setOfferAdded(0);
            userSmsConfigBean.setChangePhone(0);
            userSmsConfigBean.setChangePassword(0);
            userSmsConfigDao.insert(userSmsConfigBean);
        }
        result.setMsg(userSmsConfigBean);
    }
}