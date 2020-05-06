package com.dmg.bairenlonghu.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.common.result.MessageResult;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.service.PushService;
import com.dmg.bairenlonghu.service.cache.PlayerService;
import com.dmg.bairenlonghu.tcp.server.AbstractMessageHandler;
import com.dmg.bairenlonghu.tcp.server.MessageIdConfig;

/**
 * @Description 获取申请上庄人员信息
 * @Author mice
 * @Date 2019/8/5 16:34
 * @Version V1.0
 **/
@Service
public class GetApplyToZhuangInfoProcess implements AbstractMessageHandler {
    @Autowired
    private PlayerService playerCacheService;
    @Autowired
    private PushService pushService;

    @Override
    public String getMessageId() {
        return MessageIdConfig.GET_APPLY_TO_ZHUANG_INFO;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        BasePlayer basePlayer = this.playerCacheService.getPlayer(userId);
        Room room = RoomManager.intance().getRoom(basePlayer.getRoomId());
        MessageResult messageResult = new MessageResult(MessageIdConfig.GET_APPLY_TO_ZHUANG_INFO, room.getApplyToZhuangPlayerList());
        this.pushService.push(userId, messageResult);
    }
}