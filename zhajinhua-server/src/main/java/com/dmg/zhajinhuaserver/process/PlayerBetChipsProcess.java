package com.dmg.zhajinhuaserver.process;
import com.alibaba.fastjson.JSONObject;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.ZhaJinHuaD;
import com.dmg.zhajinhuaserver.service.PlayerBetChipsService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dmg.zhajinhuaserver.config.MessageConfig.PLAYER_PLAY;
/**
 * @Description 操作
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
@Service
public class PlayerBetChipsProcess implements AbstractMessageHandler {
    @Override
    public String getMessageId() {
        return PLAYER_PLAY;
    }
    @Autowired
    RoomService roomService;
    @Autowired
    PlayerService playerCacheService;
    @Autowired
    PlayerBetChipsService playerBetChipsService;
    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = playerCacheService.getPlayer(userId);
        int beSeatId =  params.getInteger(ZhaJinHuaD.COMPARED_SEAT)==null? 0:params.getInteger(ZhaJinHuaD.COMPARED_SEAT);
        int oper = params.getInteger(ZhaJinHuaD.ACTION_TYPE);
        long wager = params.getLong(ZhaJinHuaD.CHIPS)==null? 0:params.getLong(ZhaJinHuaD.CHIPS);
        playerBetChipsService.handleBet(player, beSeatId, oper, wager);
    }
}
