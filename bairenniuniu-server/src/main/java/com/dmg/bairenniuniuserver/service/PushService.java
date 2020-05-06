package com.dmg.bairenniuniuserver.service;

import com.dmg.bairenniuniuserver.common.result.MessageResult;
import com.dmg.bairenniuniuserver.model.Player;
import com.dmg.bairenniuniuserver.model.Room;

/**
 * @Description 消息推送
 * @Author mice
 * @Date 2019/7/1 19:37
 * @Version V1.0
 **/
public interface PushService {

    /**
     * @param userId
     * @param m
     * @param msg
     * @param res
     * @return void
     * @description: 个人推送
     * @author mice
     * @date 2019/7/1
     */
    public void push(int userId, String m, Integer res, Object msg);

    /**
     * @param userId
     * @param m
     * @param res
     * @return void
     * @description: 个人推送
     * @author mice
     * @date 2019/7/1
     */
    public void push(int userId, String m, Integer res);

    /**
     * @param userId
     * @param m
     * @return void
     * @description: 个人推送
     * @author mice
     * @date 2019/7/1
     */
    void push(int userId, String m);

    /**
     * @param userId
     * @param messageResult
     * @return void
     * @description: 个人推送
     * @author mice
     * @date 2019/7/1
     */
    void push(int userId, MessageResult messageResult);

    /**
     * @param message
     * @param room
     * @return void
     * @description: 房间内全体推送
     * @author mice
     * @date 2019/7/2
     */
    void broadcast(MessageResult message, Room room);

    /**
     * @param message
     * @param player
     * @param room
     * @return void
     * @description: 房间内广播，除开指定玩家
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