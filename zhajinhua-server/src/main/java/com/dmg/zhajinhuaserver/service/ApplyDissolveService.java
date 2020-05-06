package com.dmg.zhajinhuaserver.service;


import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description  玩家申请解散房间
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
public interface ApplyDissolveService {
    /**
     * 玩家申请解散房间
     *
     * @param player
     */
    void applyDissolveRoom(Player player);
}
