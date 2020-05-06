package com.dmg.zhajinhuaserver.service;


import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description  玩家准备
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
public interface ReadyService {
    /**
     * 准备
     *
     * @param player
     * @param ready
     */
    void ready(Player player, boolean ready);

    void checkAllReady(GameRoom room);

}
