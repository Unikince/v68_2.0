package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.result.MessageResult;

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
    public void push(Long userId, String m, Integer res, Object msg);

    /**
     * @description: 个人推送
     * @param userId
     * @param m
     * @param res
     * @return void
     * @author mice
     * @date 2019/7/1
     */
    public void push(Long userId, String m, Integer res);

    /**
     * @description: 个人推送
     * @param userId
     * @param m
     * @return void
     * @author mice
     * @date 2019/7/1
     */
    void push(Long userId, String m);

    /**
     * @description: 个人推送
     * @param userId
     * @param messageResult
     * @return void
     * @author mice
     * @date 2019/7/1
     */
    void push(Long userId, MessageResult messageResult);
}