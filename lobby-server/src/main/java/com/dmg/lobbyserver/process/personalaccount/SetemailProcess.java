package com.dmg.lobbyserver.process.personalaccount;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.UserVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.ValidateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import static com.dmg.lobbyserver.config.MessageConfig.SET_EMAIL;
/**
 * @Description 绑定邮箱
 * @Author jock
 * @Date 2019/6/19 0019
 * @Version V1.0
 **/
@Service
@Slf4j
public class SetemailProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return SET_EMAIL;
    }

    @Autowired
    ValidateCodeService validateCodeService;
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao ;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserVo userToemailVo = params.toJavaObject(UserVo.class);
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        if (!validateCodeService.validateSuccess(userToemailVo.getOldEmail(), userToemailVo.getEmailCode())) {
            result.setRes(ResultEnum.VALIDATE_CODE_ERROR.getCode());
            return;
        }
        if (!validateCodeService.expire(userToemailVo.getOldEmail())) {
            result.setRes(ResultEnum.VALIDATE_CODE_TIME_OUT.getCode());
            return;
        }
        Long countByEmail = userDao.getCountByEmail(userToemailVo.getOldEmail());
        if(countByEmail>0){
            result.setRes(ResultEnum.EMAIL_ISEXIST.getCode());
        }else{
            userBean.setEmail(userToemailVo.getOldEmail());
            userService.updateUserById(userBean);
            result.setMsg(ResultEnum.SUCCESS);
        }
    }
}
