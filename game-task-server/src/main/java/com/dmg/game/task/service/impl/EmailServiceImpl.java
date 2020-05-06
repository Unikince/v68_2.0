package com.dmg.game.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.game.task.dao.EmailDao;
import com.dmg.game.task.model.bean.EmailBean;
import com.dmg.game.task.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:03 2020/3/19
 */
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailDao emailDao;

    @Override
    public List<EmailBean> selectExpireEmail() {
        return emailDao.selectList(new LambdaQueryWrapper<EmailBean>().lt(EmailBean::getExpireDate, new Date()));
    }
}
