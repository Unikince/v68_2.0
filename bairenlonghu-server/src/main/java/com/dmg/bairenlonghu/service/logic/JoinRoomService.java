package com.dmg.bairenlonghu.service.logic;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/1 10:39
 * @Version V1.0
 **/
public interface JoinRoomService {

    /**
     * @description: 玩家加入房间
     * @param level
     * @param userId
     * @return void
     * @author mice
     * @date 2019/8/1
    */
    void playerJoinRoom(int userId,int level);

    /**
     * @description: 机器人加入房间
     * @param
     * @return void
     * @author mice
     * @date 2019/8/1
     */
    void robotJoinRoom();
}