package com.dmg.bairenzhajinhuaserver.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.tcp.server.AbstractMessageHandler;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;

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
    
    @Override
    public String getMessageId() {
        return MessageIdConfig.GET_APPLY_TO_ZHUANG_INFO;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        BasePlayer basePlayer = playerCacheService.getPlayer(userId);
        Room room = RoomManager.intance().getRoom(basePlayer.getRoomId());
        MessageResult messageResult = new MessageResult(MessageIdConfig.GET_APPLY_TO_ZHUANG_INFO,room.getApplyToZhuangPlayerList());
        basePlayer.push(messageResult);
    }
}