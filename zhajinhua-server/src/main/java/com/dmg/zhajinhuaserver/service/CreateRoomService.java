package com.dmg.zhajinhuaserver.service;



import com.dmg.zhajinhuaserver.model.bean.Player;

import java.util.Map;

/**
 * @Description创建房间
 * @Author jock
 * @Date 2019/7/10 0010
 * @Version V1.0
 **/
public interface CreateRoomService {
    /**
     * 创建房间
     *
     * @param player
     * @param ruleMap
     */
    void createRoom(Player player, Map<String, Object> ruleMap);
}
