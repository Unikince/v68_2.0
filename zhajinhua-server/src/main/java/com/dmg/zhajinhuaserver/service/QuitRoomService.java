package com.dmg.zhajinhuaserver.service;


import com.dmg.zhajinhuaserver.model.bean.Player;

/**
 * @Description 退出房间
 * @Author jock
 * @Date 2019/7/9 0009
 * @Version V1.0
 **/
public interface QuitRoomService {
    /**
     * 退出房间
     *
     * @param player
     */
    boolean quitRoom(Player player, boolean bool, int leaveReason);
}
