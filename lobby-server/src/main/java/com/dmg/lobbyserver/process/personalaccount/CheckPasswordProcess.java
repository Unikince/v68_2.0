package com.dmg.lobbyserver.process.personalaccount;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static com.dmg.lobbyserver.config.MessageConfig.CHECK_PASSWORD;
/**
 * @Description 密码验证
  * @Author jock
 * @Date 2019/7/1 0001
 * @Version V1.0
 **/
@Slf4j
@Service
public class CheckPasswordProcess implements AbstractMessageHandler {
    @Value("${md5.salt}")
    private String salt;
    @Override
    public String getMessageId() {
        return CHECK_PASSWORD;
    }
    @Autowired
    UserService userService ;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        String password = params.getString("password");
        String  pass= DigestUtil.md5Hex(password+salt);
        UserBean userById = userService.getUserById(Long.parseLong(userid));
        if(userById==null){
            result.setRes(ResultEnum.ACCOUNT_NOT_EXIST.getCode());
            return;
        }
        if(StringUtils.equals(pass,userById.getPassword())){
            result.setRes(ResultEnum.SUCCESS.getCode());
        }else{
            result.setRes(ResultEnum.PASSWORD_ERROR.getCode());
        }
    }
}
