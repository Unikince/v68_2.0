package com.dmg.lobbyserver.process.personalaccount;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import static com.dmg.lobbyserver.config.MessageConfig.SET_PASSWORD;
/**
 * @Description  设置密码
 * @Author jock
 * @Date 2019/6/21 0021
 * @Version V1.0
 **/
@Service
@Slf4j
public class SetpasswordProcess implements AbstractMessageHandler {
    @Value("${md5.salt}")
    private String salt;
    @Override
    public String getMessageId() {
        return SET_PASSWORD;
    }
@Autowired
    UserService userService  ;
    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        String password = params.getString("password");
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        userBean.setPassword(DigestUtil.md5Hex(password+salt));
        log.info("========="+password);
        log.info("========="+userBean.getPassword());
         userService.updateUserById(userBean);
    }
}
