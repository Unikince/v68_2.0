package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.UserVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.REVICE_PASSWORD;

/**
 * @Description 用户修改密码
 * @Author jock
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
@Service
@Slf4j
public class RevisepasswordProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return REVICE_PASSWORD;
    }

    @Autowired
    UserService userService;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserVo UserVo = params.toJavaObject(UserVo.class);
        UserBean user = userService.getUserById(Long.parseLong(userid));
        //判断旧密码是否与数据库一致
        if (user!=null&&StringUtils.equals(UserVo.getOldPassword(),user.getPassword())) {
            user.setPassword(UserVo.getNewPassword());
            userService.updateUserById(user);
        } else {
            result.setRes(ResultEnum.PASSWORD_ISSAME.getCode());
        }
    }
}
