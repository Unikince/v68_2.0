package com.dmg.niuniuserver.process;

import static com.dmg.niuniuserver.config.MessageConfig.READY;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.service.action.ReadyService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description 准备
 * @Author mice
 * @Date 2019/7/2 9:34
 * @Version V1.0
 **/
@Slf4j
@Service
public class ReadyProcess implements AbstractMessageHandler {
    @Autowired
    private ReadyService readyService;
    @Autowired
    private PlayerService playerService;

    @Override
    public String getMessageId() {
        return READY;
    }

    @Override
    public void messageHandler(Long userId, JSONObject params) {
        Player player = this.playerService.getPlayer(userId);
        this.readyService.ready(player, true, false);
        this.playerService.update(player);
    }
}