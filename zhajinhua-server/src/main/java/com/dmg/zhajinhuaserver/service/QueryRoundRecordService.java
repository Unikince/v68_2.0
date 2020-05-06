package com.dmg.zhajinhuaserver.service;


import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description
 * @Author jock
 * @Date 2019/7/3 13:43
 * @Version V1.0
 **/
public interface QueryRoundRecordService {

    /**
     *
     * @param player
     * @param type
     */
    void queryRoundRecord(Player player, int type);
}
