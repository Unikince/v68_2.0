package com.dmg.zhajinhuaserver.service;


import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description 加入房间
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
public interface JoinRoomService {
    /**
     * 加入房间
     *
     * @param player
     * @param roomId
     */
    void joinRoom(Player player, int roomId);
}
