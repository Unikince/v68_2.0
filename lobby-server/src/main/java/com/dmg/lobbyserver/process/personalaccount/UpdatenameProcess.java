package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.USER_NAME;
/**
 * @Description  设置真实姓名
 * @Author jock
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
@Service
@Slf4j
public class UpdatenameProcess implements AbstractMessageHandler {
    @Autowired
    UserService userService;

    @Override
    public String getMessageId() {
        return USER_NAME;
    }
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        String realName = params.getString("RealName");
        UserBean user=userService.getUserById(Long.parseLong(userid));
        user.setRealName(realName);
        userService.updateUserById(user);
    }
}
