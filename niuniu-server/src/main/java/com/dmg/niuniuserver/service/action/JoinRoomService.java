package com.dmg.niuniuserver.service.action;

import com.dmg.niuniuserver.model.bean.Player;

/**
 * @Description 加入房间
 * @Author mice
 * @Date 2019/7/2 18:48
 * @Version V1.0
 **/
public interface JoinRoomService {

    /**
     * @description: 加入房间
     * @param player
     * @param roomId
     * @return void
     * @author mice
     * @date 2019/7/2
    */
    void joinRoom(Player player, int roomId);
}