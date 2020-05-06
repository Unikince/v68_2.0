package com.dmg.niuniuserver.service.action;

import com.dmg.niuniuserver.model.bean.Player;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/4 14:49
 * @Version V1.0
 **/
public interface SitDownService {

    /**
     * @description: 玩家坐下
     * @param player
     * @return void
     * @author mice
     * @date 2019/7/4
    */
    void sitDown(Player player);
}