package com.dmg.game.task.service;

import com.dmg.game.task.model.bean.EmailBean;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:03 2020/3/19
 */
public interface EmailService {

    /**
     * @Author liubo
     * @Description 查询过期邮件//TODO
     * @Date 11:05 2020/3/19
     **/
    List<EmailBean> selectExpireEmail();
}
