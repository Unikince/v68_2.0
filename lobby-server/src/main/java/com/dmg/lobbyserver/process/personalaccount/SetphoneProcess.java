package com.dmg.lobbyserver.process.personalaccount;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.*;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.UserVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.UserTaskProgressService;
import com.dmg.lobbyserver.service.ValidateCodeService;
import com.dmg.server.common.enums.TaskTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.dmg.lobbyserver.config.MessageConfig.BINDING_PHONE;

/**
 * @Description 绑定手机
 * @Author jock
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
@Slf4j
@Service
public class SetphoneProcess implements AbstractMessageHandler {

    @Override
    public String getMessageId() {
        return BINDING_PHONE;
    }

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private UserTaskProgressService userTaskProgressService;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserVo userVo = params.toJavaObject(UserVo.class);
        if (!validateCodeService.validateSuccess(userVo.getPhone(), userVo.getCode())) {
            result.setRes(ResultEnum.VALIDATE_CODE_ERROR.getCode());
            return;
        }
        if (!validateCodeService.expire(userVo.getPhone())) {
            result.setRes(ResultEnum.VALIDATE_CODE_TIME_OUT.getCode());
            return;
        }
        //判断手机号是否重复
        Long countByPhone = userDao.getCountByPhone(userVo.getPhone());
        if (countByPhone == 0) {
            UserBean userBean = userService.getUserById(Long.parseLong(userid));
            userBean.setPhone(userVo.getPhone());
            userService.updateUserById(userBean);
            userTaskProgressService.userTaskChange(userBean.getId(), TaskTypeEnum.CODE_BINDING_PHONE.getCode(), 1, null);
        } else {
            result.setRes(ResultEnum.PHONE_HAS_BE_USE.getCode());
        }
    }
}

