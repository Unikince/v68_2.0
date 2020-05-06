package com.dmg.bairenniuniuserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.bairenniuniuserver.common.result.MessageResult;
import com.dmg.bairenniuniuserver.manager.RoomManager;
import com.dmg.bairenniuniuserver.service.cache.impl.PlayerCacheService;
import com.dmg.bairenniuniuserver.tcp.server.MessageIdConfig;
import com.dmg.data.client.net.DataPushHandler;
import com.dmg.data.common.constant.MsgConst;
import com.dmg.data.common.dto.GoldPayPushDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author mice
 * @Date 2019/12/11 13:45
 * @Version V1.0
 **/
@Component(MsgConst.goldPay)
public class RechargeHandler implements DataPushHandler {

    @Autowired
    private PushService pushService;
    @Autowired
    private PlayerCacheService playerCacheService;

    @Override
    public void action(String msg) {
        GoldPayPushDto goldPayPushDto = JSON.parseObject(msg, GoldPayPushDto.class);
        BasePlayer basePlayer = playerCacheService.getPlayer(goldPayPushDto.getUserId());
        if (basePlayer == null) {
            return;
        }
        basePlayer.setGold(basePlayer.getGold().add(goldPayPushDto.getPayGold()));
        playerCacheService.updatePlayer(basePlayer);
        int seatIndex = -1;
        if (basePlayer.getRoomId() > 0) {
            seatIndex = RoomManager.intance().addPlayerGold(basePlayer.getRoomId(), goldPayPushDto.getUserId(), goldPayPushDto.getPayGold());
        }
        JSONObject json = new JSONObject();
        json.put("seatIndex", seatIndex);
        json.put("playerGold", basePlayer.getGold());
        MessageResult messageResult = new MessageResult(MessageIdConfig.SYNC_GOLD,json);
        if (basePlayer.getRoomId() > 0) {
            pushService.broadcast(messageResult, RoomManager.intance().getRoom(basePlayer.getRoomId()));
        }else {
            pushService.push(basePlayer.getUserId(),messageResult);
        }

    }
}