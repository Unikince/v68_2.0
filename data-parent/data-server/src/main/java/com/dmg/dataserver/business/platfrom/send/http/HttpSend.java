package com.dmg.dataserver.business.platfrom.send.http;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.dmg.dataserver.business.model.BaseUser;
import com.dmg.dataserver.business.model.User;
import com.dmg.dataserver.business.platfrom.model.UserDTO;
import com.dmg.dataserver.common.platfrom.HttpClient;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class HttpSend {
    @Autowired
    private HttpClient client;

    public BaseUser getUser(long userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        String dataStr = this.client.get("/syncUserInfo", params);
        if (StringUtils.isEmpty(dataStr)) {
            log.error("获取[{}]用户信息失败", userId);
            return null;
        }
        UserDTO userDTO = JSON.parseObject(dataStr).toJavaObject(UserDTO.class);
        BaseUser user = new User();
        user.setId(userDTO.getId());
        user.setNickname(userDTO.getUserName());
        user.setHeadImage(userDTO.getHeadImage());
        user.setSex(userDTO.getSex());
        user.setGold((userDTO.getAccountBalance()));
        return user;
    }
}