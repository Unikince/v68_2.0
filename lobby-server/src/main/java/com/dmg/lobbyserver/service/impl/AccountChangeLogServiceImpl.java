package com.dmg.lobbyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.AccountChangeLogDao;
import com.dmg.lobbyserver.dao.bean.AccountChangeLogBean;
import com.dmg.lobbyserver.service.AccountChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:59 2019/12/30
 */
@Service
public class AccountChangeLogServiceImpl implements AccountChangeLogService {

    @Autowired
    private AccountChangeLogDao accountChangeLogDao;

    @Override
    public void insertList(List<AccountChangeLogBean> accountChangeLogBeanList) {
        accountChangeLogDao.insertBatch(accountChangeLogBeanList);
    }

    @Override
    public List<AccountChangeLogBean> getWeekChange(Long userId) {
        return accountChangeLogDao.getWeekChange(userId);
    }

    @Override
    public List<AccountChangeLogBean> getListByUserId(Long userId) {
        return accountChangeLogDao.selectList(new LambdaQueryWrapper<AccountChangeLogBean>()
                .eq(AccountChangeLogBean::getUserId, userId));
    }
}
