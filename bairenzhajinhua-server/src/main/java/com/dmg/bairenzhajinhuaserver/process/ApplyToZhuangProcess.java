package com.dmg.bairenzhajinhuaserver.process;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.manager.RoomManager;
import com.dmg.bairenzhajinhuaserver.model.Room;
import com.dmg.bairenzhajinhuaserver.model.Seat;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.tcp.server.AbstractMessageHandler;
import com.dmg.bairenzhajinhuaserver.tcp.server.MessageIdConfig;

/**
 * @Description 申请上庄
 * @Author mice
 * @Date 2019/8/5 16:34
 * @Version V1.0
 **/
@Service
public class ApplyToZhuangProcess implements AbstractMessageHandler {
    @Autowired
    private PlayerService playerCacheService;

    @Override
    public String getMessageId() {
        return MessageIdConfig.APPLY_TO_ZHUANG;
    }

    @Override
    public void messageHandler(int userId, JSONObject params) {
        BasePlayer basePlayer = playerCacheService.getPlayer(userId);
        RoomManager manager = RoomManager.intance();
        Room room = manager.getRoom(basePlayer.getRoomId());
        int upGold = manager.getBankerLimitMap().get(room.getLevel()+"");
        if (basePlayer.getGold().compareTo(new BigDecimal(upGold)) < 0) {
        	basePlayer.push(MessageIdConfig.APPLY_TO_ZHUANG, ResultEnum.GOLD_NEED_10000.getCode());
            return;
        }
        synchronized (room) {
        	for (BasePlayer basePlayer1 : room.getApplyToZhuangPlayerList()){
	            if (basePlayer1.getUserId()==userId){
	            	basePlayer.push(MessageIdConfig.APPLY_TO_ZHUANG, ResultEnum.HAS_APPLY_SHANGZHUANG.getCode());
	                return;
	            }
	        }
        	if(room.getBanker().getPlayer().getUserId() == basePlayer.getUserId()) {
        		basePlayer.push(MessageIdConfig.APPLY_TO_ZHUANG, ResultEnum.WITH_SHANGZHUANG.getCode());
                return;
        	}
        	room.getApplyToZhuangPlayerList().add(basePlayer);
		}
        Seat seat = manager.getSeat(room, userId);
        seat.setApplyBanker(true);
        basePlayer.push(MessageIdConfig.APPLY_TO_ZHUANG);
    }
}