package com.dmg.lobbyserver.service;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/19 16:15
 * @Version V1.0
 **/
public interface SendEmailService {

    /**
     * @description:
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return void
     * @author mice
     * @date 2019/6/19
     */
    void send(String to, String subject, String content);
}