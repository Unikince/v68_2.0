package com.dmg.redblackwarserver.service;

import com.dmg.redblackwarserver.common.result.MessageResult;
import com.dmg.redblackwarserver.model.Player;
import com.dmg.redblackwarserver.model.Room;

/**
 * @Description 消息推送
 * @Author mice
 * @Date 2019/7/1 19:37
 * @Version V1.0
 **/
public interface PushService {

    /**
     * @description: 个人推送
     * @param userId
     * @param m
     * @param msg
     * @param res
     * @return void
     * @author mice
     * @date 2019/7/1
    */
    public void push(int userId, String m, Integer res, Object msg);

    /**
     * @description: 个人推送
     * @param userId
     * @param m
     * @param res
     * @return void
     * @author mice
     * @date 2019/7/1
     */
    public void push(int userId, String m, Integer res);

    /**
     * @description: 个人推送
     * @param userId
     * @param m
     * @return void
     * @author mice
     * @date 2019/7/1
     */
    void push(int userId, String m);

    /**
     * @description: 个人推送
     * @param userId
     * @param messageResult
     * @return void
     * @author mice
     * @date 2019/7/1
     */
    void push(int userId, MessageResult messageResult);

    /**
     * @description: 房间内全体推送
     * @param message
     * @param room
     * @return void
     * @author mice
     * @date 2019/7/2
    */
    void broadcast(MessageResult message, Room room);

   /**
    * @description: 房间内广播，除开指定玩家
    * @param message
    * @param player
    * @param room
    * @return void
    * @author mice
    * @date 2019/7/2
   */
   void broadcastWithOutPlayer(MessageResult message, Player player, Room room);

    /**
     * @Author liubo
     * @Description //TODO 全连接广播
     * @Date 11:01 2020/1/10
     **/
    void broadcast(MessageResult message);



}