package com.dmg.lobbyserver.process.activity.bind;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.SysItemConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDao;
import com.dmg.lobbyserver.dao.SysRewardConfigDetailDao;
import com.dmg.lobbyserver.dao.bean.SysItemConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigBean;
import com.dmg.lobbyserver.dao.bean.SysRewardConfigDetailBean;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.BindParameterDTO;
import com.dmg.lobbyserver.model.dto.GiftDataDTO;
import com.dmg.lobbyserver.process.AbstractMessageHandler;
import com.dmg.lobbyserver.process.activity.UserProessService;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.result.ResultEnum;
import com.dmg.lobbyserver.service.GiftDataService;
import com.dmg.lobbyserver.service.UserService;
import com.dmg.lobbyserver.service.ValidateCodeService;
import com.zyhy.common_server.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dmg.lobbyserver.config.MessageConfig.BIND_GET;

/**
 * 绑定手机送礼
 * Author:刘将军
 * Time:2019/6/19 18:46
 * Created by IntelliJ IDEA Community
 */
@Slf4j
@Service
@SuppressWarnings("all")
public class BindGetProcess implements AbstractMessageHandler {

    @Autowired
    private UserService userService;
    @Autowired
    private SysItemConfigDao sysItemConfigDao;
    @Autowired
    private UserProessService userProessService;
    @Autowired
    private SysRewardConfigDao sysRewardConfigDao;
    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private SysRewardConfigDetailDao sysRewardConfigDetailDao;
    @Autowired
    private GiftDataService giftDataService;

    @Override
    public String getMessageId() {
        return BIND_GET;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {

    }
}
