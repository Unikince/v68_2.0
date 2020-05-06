package com.dmg.clubserver.process;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.clubserver.dao.ClubDao;
import com.dmg.clubserver.dao.RClubPlayerDao;
import com.dmg.clubserver.dao.bean.ClubBean;
import com.dmg.clubserver.dao.bean.RClubPlayerBean;
import com.dmg.clubserver.model.RoleInfoBean;
import com.dmg.clubserver.model.dto.ClubLoginDTO;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.result.ResultEnum;
import com.dmg.clubserver.service.SynchronousPlayerDataService;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dmg.clubserver.config.MessageConfig.CLUB_LOGIN;

/**
 * @Description 俱乐部登录
 * @Author mice
 * @Date 2019/5/25 16:00
 * @Version V1.0
 **/
@Service
public class TestProcess implements AbstractMessageHandler{
    @Autowired
    private SynchronousPlayerDataService synchronousPlayerDataService;
    @Autowired
    private ClubDao clubDao;
    @Autowired
    private RClubPlayerDao clubPlayerDao;
    @Autowired
    private WebSocketClient webSocketClient;

    @Override
    public String getMessageId() {
        return "1";
    }


    @Override
    public void messageHandler(String userid,JSONObject params,MessageResult result) {
        webSocketClient.send(params.toJSONString());
    }
}