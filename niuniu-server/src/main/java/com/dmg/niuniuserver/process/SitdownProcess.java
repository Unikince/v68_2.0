package com.dmg.niuniuserver.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.config.MessageConfig;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.service.action.SitDownService;

/**
 * @Description 坐下 不再使用
 * @Author mice
 * @Date 2019/7/3 9:42
 * @Version V1.0
 **/
@Service
public class SitdownProcess implements AbstractMessageHandler {
    @Autowired
    private SitDownService sitDownService;
    @Autowired
    private PlayerService playerService;

    @Override
    public String getMessageId() {
        return MessageConfig.SITDOWN;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = this.playerService.getPlayer(userId);
        this.sitDownService.sitDown(player);
        this.playerService.update(player);
    }
}