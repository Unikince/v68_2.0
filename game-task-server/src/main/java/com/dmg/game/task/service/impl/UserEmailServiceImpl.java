package com.dmg.game.task.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.game.task.dao.UserEmailDao;
import com.dmg.game.task.model.bean.UserEmailBean;
import com.dmg.game.task.service.UserEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 17:12 2020/3/20
 */
@Service
public class UserEmailServiceImpl implements UserEmailService {

    @Autowired
    private UserEmailDao userEmailDao;

    @Override
    public void clearExpireEmail() {
        userEmailDao.delete(new LambdaQueryWrapper<UserEmailBean>().lt(UserEmailBean::getExpireDate, new Date()));
    }
}
