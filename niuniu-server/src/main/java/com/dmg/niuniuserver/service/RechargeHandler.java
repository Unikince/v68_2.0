package com.dmg.niuniuserver.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.data.client.net.DataPushHandler;
import com.dmg.data.common.constant.MsgConst;
import com.dmg.data.common.dto.GoldPayPushDto;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.platform.service.PlayerCacheService;
import com.dmg.niuniuserver.result.MessageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
        Player basePlayer = playerCacheService.getPlayer(goldPayPushDto.getUserId());
        if (basePlayer == null) {
            return;
        }
        basePlayer.setGold(BigDecimal.valueOf(basePlayer.getGold()).add(goldPayPushDto.getPayGold()).doubleValue());
        playerCacheService.update(basePlayer);
        int seatIndex = 0;
        if (basePlayer.getRoomId() > 0) {
            seatIndex = RoomManager.instance().addPlayerGold(basePlayer.getRoomId(), goldPayPushDto.getUserId(), goldPayPushDto.getPayGold());
        }
        if (seatIndex == 0) {
            return;
        }
        JSONObject json = new JSONObject();
        json.put("seatIndex", seatIndex);
        json.put("playerGold", basePlayer.getGold());
        MessageResult messageResult = new MessageResult(MessageConfig.SYNC_GOLD, json);
        pushService.broadcast(messageResult, RoomManager.instance().getRoom(basePlayer.getRoomId()));
    }
}