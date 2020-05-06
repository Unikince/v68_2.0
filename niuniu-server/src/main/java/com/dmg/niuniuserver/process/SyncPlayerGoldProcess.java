package com.dmg.niuniuserver.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.model.vo.SyncPlayerGoldVO;
import com.dmg.niuniuserver.platform.service.PlayerService;

/**
 * @Description 同步用户金币
 * @Author mice
 * @Date 2019/7/4 17:24
 * @Version V1.0
 **/
@Service
public class SyncPlayerGoldProcess implements AbstractMessageHandler {
    @Autowired
    private PlayerService playerService;

    @Override
    public String getMessageId() {
        return MessageConfig.SYNC_PLAYER_GOLD;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        SyncPlayerGoldVO vo = params.toJavaObject(SyncPlayerGoldVO.class);
        Player player = this.playerService.getPlayer(vo.getUserId());
        player.setGold(vo.getGold());
        this.playerService.update(player);
    }
}