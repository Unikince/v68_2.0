package com.dmg.zhajinhuaserver.service;


/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 13:43
 * @Version V1.0
 **/
public interface IdGeneratorService {

    /**
     * @description: 获取房间id
     * @param
     * @return java.lang.Integer
     * @author mice
     * @date 2019/7/3
    */
    Integer getRoomId();


    /**
     * @description: 获取游戏编号
     * @param
     * @return java.lang.Integer
     * @author mice
     * @date 2019/7/16
    */
    Integer getGameNum();



}