package com.dmg.lobbyserver.process;

import com.alibaba.fastjson.JSONObject;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.dao.bean.UserBean;
import com.dmg.lobbyserver.model.dto.LobbyActivityInfoDTO;
import com.dmg.lobbyserver.result.MessageResult;
import com.dmg.lobbyserver.service.PlatformRechargeLogService;
import com.dmg.lobbyserver.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.lobbyserver.config.MessageConfig.LOBBY_ACTIVITY_INFO;

/**
 * @Description 获取大厅活动开放信息
 * @Author mice
 * @Date 2019/6/19 17:49
 * @Version V1.0
 **/
@Service
public class LobbyActivityInfoProcess implements AbstractMessageHandler{
    @Autowired
    private UserService userService;
    @Autowired
    private PlatformRechargeLogService platformRechargeLogService;
    @Override
    public String getMessageId() {
        return LOBBY_ACTIVITY_INFO;
    }

    @Override
    public void messageHandler(String userid, JSONObject params, MessageResult result) {
        UserBean userBean = userService.getUserById(Long.parseLong(userid));
        LobbyActivityInfoDTO lobbyActivityInfoDTO = new LobbyActivityInfoDTO();
        if (!StringUtils.isEmpty(userBean.getPhone())){
            lobbyActivityInfoDTO.setBindPhone(false);
        }
        if (userBean.getNewUser()==0){
            lobbyActivityInfoDTO.setNewUser(false);
        }
        lobbyActivityInfoDTO.setWithdraw(platformRechargeLogService.playerHasRecharge(Long.parseLong(userid)));
        result.setMsg(lobbyActivityInfoDTO);
    }
}