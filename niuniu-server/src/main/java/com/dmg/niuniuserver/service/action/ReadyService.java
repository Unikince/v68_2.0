package com.dmg.niuniuserver.service.action;

import com.dmg.niuniuserver.model.bean.Player;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/4 14:16
 * @Version V1.0
 **/
public interface ReadyService {

    /**
     * @description: 玩家准备
     * @param player
     * @param ready
     * @param automaticReady
     * @return void
     * @author mice
     * @date 2019/7/4
    */
    void ready(Player player, boolean ready, boolean automaticReady);
}