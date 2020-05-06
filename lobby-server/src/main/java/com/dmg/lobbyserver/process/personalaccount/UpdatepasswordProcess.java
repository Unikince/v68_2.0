package com.dmg.lobbyserver.process.personalaccount;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.UserVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class UpdatepasswordProcess implements AbstractMessageHandler {
    @Value("${md5.salt}")
    private String salt;

    @Override
    public String getMessageId() {
        return REVICE_PASSWORD;
    }

    @Autowired
    UserService userService;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserVo userVo = params.toJavaObject(UserVo.class);
        UserBean user = userService.getUserById(Long.parseLong(userid));
        //判断旧密码是否与数据库一致
        String oldPass = DigestUtil.md5Hex(userVo.getOldPassword() + salt);
        String newPas = DigestUtil.md5Hex(userVo.getNewPassword() + salt);
        if (user == null) {
            result.setRes(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            return;
        }
        log.info("{},oldPass:{},vo.oldPass:{},user.Pass:{}{},newPas:{}",userVo,oldPass,user.getPassword(),newPas);
        if (!StringUtils.equals(oldPass, user.getPassword())) {
            result.setRes(ResultEnum.PASSWORD_ISSAME.getCode());
            return;
        }
        if (oldPass.equals(newPas)) {
            result.setRes(ResultEnum.PASSWORD_IS_NOT_SAME.getCode());
            return;
        }
        user.setPassword(newPas);
        userService.updateUserById(user);
    }
}
