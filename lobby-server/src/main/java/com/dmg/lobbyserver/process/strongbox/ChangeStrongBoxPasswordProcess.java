package com.dmg.lobbyserver.process.strongbox;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.ChangeStrongBoxPasswordVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.ValidateCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.CHANGE_STRONG_BOX_PASSWORD;
import static com.dmg.lobbyserver.result.ResultEnum.*;

/**
 * @Description 设置保险箱密码
 * @Author mice
 * @Date 2019/6/20 16:38
 * @Version V1.0
 **/
@Service
public class ChangeStrongBoxPasswordProcess implements AbstractMessageHandler {

    @Value("${md5.salt}")
    private String salt;
    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private UserService userService;
    @Override
    public String getMessageId() {
        return CHANGE_STRONG_BOX_PASSWORD;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        ChangeStrongBoxPasswordVO vo = params.toJavaObject(ChangeStrongBoxPasswordVO.class);
        UserBean userBean = userService.getUserById(vo.getUserId());
        if (userBean==null){
            result.setRes(PARAM_ERROR.getCode());
            return;
        }
        if(!validateCodeService.validateSuccess(vo.getPhone(),vo.getValidateCode())){
            result.setRes(VALIDATE_CODE_ERROR.getCode());
            return;
        }
        if (!validateCodeService.expire(vo.getPhone())){
            result.setRes(VALIDATE_CODE_TIME_OUT.getCode());
            return;
        }
        userBean.setStrongboxPassword(DigestUtil.md5Hex(vo.getPassword()+salt));
        userService.updateUserById(userBean);
    }
}