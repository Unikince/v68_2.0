package com.dmg.lobbyserver.process.personalaccount;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserSmsConfigDao;
import com.dmg.lobbyserver.dao.bean.UserSmsConfigBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_SMS_CONFIG;
/**
 * @Description 更新用户短信设置
 * @Author mice
 * @Date 2019/6/21 17:40
 * @Version V1.0
 **/
@Service
public class UpdateUserSmsConfigProcess implements AbstractMessageHandler {
    @Autowired
    private UserSmsConfigDao userSmsConfigDao;
    @Override
    public String getMessageId() {
        return UPDATE_USER_SMS_CONFIG;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserSmsConfigBean userSmsConfigBean = params.toJavaObject(UserSmsConfigBean.class);
        userSmsConfigDao.update(userSmsConfigBean,new LambdaQueryWrapper<UserSmsConfigBean>().eq(UserSmsConfigBean::getUserId,userSmsConfigBean.getUserId()));
    }
}