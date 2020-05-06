package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_USER_ICON;

/**
 * @Description 修改头像, 性别
 * @Author jock
 * @Date 18:01
 * @Version V1.0
 **/
@Service
@Slf4j
public class UpdateIconProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return UPDATE_USER_ICON;
    }

    @Autowired
    UserService userService;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        String image = params.getString("image");
        Integer sex = params.getInteger("sex");
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        if (userBean == null) {
            result.setRes(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            return;
        }
        if (!StringUtils.isEmpty(image)) {
            userBean.setHeadImage(image);
        }
        if (!StringUtils.isEmpty(sex + "")) {
            userBean.setSex(sex);
        }
        log.info("UpdateIconProcess===="+JSONObject.toJSONString(userBean));
        userService.updateUserById(userBean);
    }
}
