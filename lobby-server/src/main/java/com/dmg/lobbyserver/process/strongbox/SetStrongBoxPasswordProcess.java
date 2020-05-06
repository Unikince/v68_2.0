package com.dmg.lobbyserver.process.strongbox;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.vo.SetStrongBoxPasswordVO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.SET_STRONG_BOX_PASSWORD;
import static com.dmg.lobbyserver.result.ResultEnum.PARAM_ERROR;

/**
 * @Description 设置保险箱密码
 * @Author mice
 * @Date 2019/6/20 16:38
 * @Version V1.0
 **/
@Service
public class SetStrongBoxPasswordProcess implements AbstractMessageHandler {

    @Value("${md5.salt}")
    private String salt;

    @Autowired
    private UserService userService;
    @Override
    public String getMessageId() {
        return SET_STRONG_BOX_PASSWORD;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        SetStrongBoxPasswordVO vo = params.toJavaObject(SetStrongBoxPasswordVO.class);
        UserBean userBean = userService.getUserById(vo.getUserId());
        if (userBean==null){
            result.setRes(PARAM_ERROR.getCode());
            return;
        }
        userBean.setStrongboxPassword(DigestUtil.md5Hex(vo.getPassword()+salt));
        userService.updateUserById(userBean);
    }
}