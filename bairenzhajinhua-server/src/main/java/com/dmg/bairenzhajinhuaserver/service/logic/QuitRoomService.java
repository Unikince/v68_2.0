package com.dmg.bairenzhajinhuaserver.service.logic;

/**
 * @Description 退出房间
 * @Author mice
 * @Date 2019/8/6 14:27
 * @Version V1.0
 **/
public interface QuitRoomService {

    /**
     * @description: 退出房间
     * @param userId
     * @return void
     * @author mice
     * @date 2019/8/6
    */
    void quitRoom(int userId,boolean kick);

}