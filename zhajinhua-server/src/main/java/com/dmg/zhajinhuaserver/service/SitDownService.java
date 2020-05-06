package com.dmg.zhajinhuaserver.service;


import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description  坐下
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
public interface SitDownService {
    /**
     * 坐下
     *
     * @param player
     * @param seat
     *            --座位号，如果座位号为0表示站起，座位号不为0，表示坐到对应位置
     */
    void sitDown(Player player, int seat);
}
