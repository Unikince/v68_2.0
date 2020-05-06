package com.dmg.lobbyserver.process.personalaccount;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import static com.dmg.lobbyserver.config.MessageConfig.SET_BIRTH;
/**
 * @Description   设置生日
 * @Author jock
 * @Date 2019/6/18 0018
 * @Version V1.0
 **/
@Slf4j
@Service
public class SetbirthProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return SET_BIRTH;
    }
    @Autowired
    UserService userService;

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        Long birth = params.getLong("Birth");
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        userBean.setBirthday(new Date(birth));
        userService.updateUserById(userBean);
    }
}