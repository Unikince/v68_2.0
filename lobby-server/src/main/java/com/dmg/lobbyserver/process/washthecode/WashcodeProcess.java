package com.dmg.lobbyserver.process.washthecode;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.SysVipPrivilegeConfigDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.dmg.lobbyserver.config.MessageConfig.CODE_WASH;

/**
 * @Description 洗码
 * @Author jock
 * @Date 2019/6/20 0020
 * @Version V1.0
 **/
@Slf4j
@Service
public class WashcodeProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return CODE_WASH;
    }

    @Autowired
    UserDao userDao;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        SysVipPrivilegeConfigDTO sysVipPrivilegeConfigDTO = params.toJavaObject(SysVipPrivilegeConfigDTO.class);
        UserBean userBean = userDao.selectOne(new LambdaQueryWrapper<UserBean>().eq(UserBean::getId, userid));
        BigDecimal monney = sysVipPrivilegeConfigDTO.getMonney();
        userBean.setAccountBalance(userBean.getAccountBalance().add(monney));
        int i = userDao.updateById(userBean);
        if (i > 0) {
            result.setRes(ResultEnum.SUCCESS.getCode());
        } else {
            result.setRes(ResultEnum.SQL_UPDATE_FAIL.getCode());
        }

    }
}
