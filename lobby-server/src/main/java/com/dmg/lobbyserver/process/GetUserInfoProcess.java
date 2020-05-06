package com.dmg.lobbyserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.UserDTO;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.GET_USER_INFO;

/**
 * @Description 获取用户信息
 * @Author mice
 * @Date 2019/6/19 10:55
 * @Version V1.0
 **/
@Service
public class GetUserInfoProcess implements AbstractMessageHandler{
    @Autowired
    private UserService userService;
    @Override
    public String getMessageId() {
        return GET_USER_INFO;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userBean,userDTO);
        if (StringUtils.isNotEmpty(userBean.getStrongboxPassword())){
            userDTO.setStrongboxPassword(true);
        }else {
            userDTO.setStrongboxPassword(false);
        }
        if (StringUtils.isEmpty(userBean.getPassword())){
            userDTO.setPassword(0+"");
        }else {
            userDTO.setPassword(1+"");
        }
        result.setMsg(userDTO);
    }
}