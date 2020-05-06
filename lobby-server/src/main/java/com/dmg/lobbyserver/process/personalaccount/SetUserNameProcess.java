package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.SET_USERNAME;
/**
 * @Description 设置游戏名称
 * @Author jock
 * @Date 2019/6/19 0019
 * @Version V1.0
 **/
@Slf4j
@Service
public class SetUserNameProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return SET_USERNAME;
    }
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
       String UserName = params.getString("UserName");
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        Integer integer = userDao.countName(UserName);
        if (integer !=0) {
            result.setRes(ResultEnum.ACCOUNT_HAS_EXIST.getCode());
        } else {
            userBean.setUserName(UserName);
            userService.updateUserById(userBean);
        }
    }
}
