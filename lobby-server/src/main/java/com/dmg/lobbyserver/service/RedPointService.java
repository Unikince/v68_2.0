package com.dmg.lobbyserver.service;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/19 18:29
 * @Version V1.0
 **/
public interface RedPointService {

    /**
     * @description: 红点推送
     * @param userId
     * @param redPointType
     * @param show
     * @return int
     * @author mice
     * @date 2019/6/5
     */
    void push(Long userId,String redPointType,boolean show);
}