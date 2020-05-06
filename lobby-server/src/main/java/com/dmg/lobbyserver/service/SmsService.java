package com.dmg.lobbyserver.service;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/21 18:02
 * @Version V1.0
 **/
public interface SmsService {

    /**
     * @description: 存款到账提醒
     * @param phoneNumber
     * @param userName
     * @param depositNum
     * @return void
     * @author mice
     * @date 2019/6/21
    */
    void depositNotify(String phoneNumber, String userName ,Long depositNum);

    /**
     * @description: 提款到账提醒
     * @param phoneNumber
     * @param userName
     * @param withdrawalNum
     * @return void
     * @author mice
     * @date 2019/6/21
    */
    void withdrawalNotify(String phoneNumber, String userName ,Long withdrawalNum);
}