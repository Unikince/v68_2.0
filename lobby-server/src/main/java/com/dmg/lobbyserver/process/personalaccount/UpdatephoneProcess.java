package com.dmg.lobbyserver.process.personalaccount;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.UserPhoneVo;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.ValidateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static com.dmg.lobbyserver.config.MessageConfig.UPDATE_PHONE;
/**
 * @Description 修改手机号码
 * @Author jock
 * @Date 2019/6/19 0019
 * @Version V1.0
 **/
@Service
@Slf4j
public class UpdatephoneProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return UPDATE_PHONE;
    }

    @Autowired
    UserService userService;
    @Autowired
    ValidateCodeService validateCodeService;
    @Autowired
    UserDao userDao;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserBean user = userService.getUserById(Long.parseLong(userid));
        UserPhoneVo UserPhoneVo = params.toJavaObject(UserPhoneVo.class);
        if (!validateCodeService.validateSuccess(UserPhoneVo.getNewPhone(), UserPhoneVo.getVerification())) {
            result.setRes(ResultEnum.VALIDATE_CODE_ERROR.getCode());
            return;
        }
        if (!validateCodeService.expire(UserPhoneVo.getNewPhone())) {
            result.setRes(ResultEnum.VALIDATE_CODE_TIME_OUT.getCode());
            return;
        }
        //旧手机号与原手机号一致
        if (!UserPhoneVo.getOldPhone().equals(user.getPhone())) {
            result.setRes(ResultEnum.OLDE_PHONE_IS_NOT_SAME.getCode());
            return;
        }
        //修改的新手机号是否与旧手机号相同
        if (UserPhoneVo.getNewPhone().equals(user.getPhone())) {
            result.setRes(ResultEnum.PHONE_ISSAME_ASNEWPHONE.getCode());
            return;
        }
        //手机号是否有人注册
        Long countByPhone = userDao.getCountByPhone(UserPhoneVo.getNewPhone());
        if (countByPhone > 0) {
            result.setRes(ResultEnum.PHONE_HAS_BE_USE.getCode());
            return;
        }
        user.setPhone(UserPhoneVo.getNewPhone());
        userService.updateUserById(user);
    }
}
