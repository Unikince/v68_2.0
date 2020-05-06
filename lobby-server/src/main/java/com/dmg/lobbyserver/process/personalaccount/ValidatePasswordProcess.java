package com.dmg.lobbyserver.process.personalaccount;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.UserSmsConfigDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.dao.bean.UserSmsConfigBean;
import com.dmg.lobbyserver.model.vo.ValidatePasswordVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.VALIDATE_PASSWORD;
import static com.dmg.lobbyserver.result.ResultEnum.PASSWORD_ERROR;

/**
 * @Description 验证用户密码
 * @Author mice
 * @Date 2019/6/21 17:40
 * @Version V1.0
 **/
@Service
public class ValidatePasswordProcess implements AbstractMessageHandler {
    @Value("${md5.salt}")
    private String salt;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserSmsConfigDao userSmsConfigDao;

    @Override
    public String getMessageId() {
        return VALIDATE_PASSWORD;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ValidatePasswordVO vo = params.toJavaObject(ValidatePasswordVO.class);
        UserBean userBean = userDao.selectById(vo.getUserId());
        if (!StringUtils.equals(userBean.getPassword(), DigestUtil.md5Hex(vo.getPassword()+salt))){
            result.setRes(PASSWORD_ERROR.getCode());
            return;
        }
    }
}