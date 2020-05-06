package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.dao.bean.AccountChangeLogBean;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 10:02 2019/12/30
 */
public interface AccountChangeLogService {

    /**
     * @Author liubo
     * @Description //TODO 批量插入
     * @Date 11:02 2019/12/30
     **/
    void insertList(List<AccountChangeLogBean> accountChangeLogBeanList);

    /**
     * @Author liubo
     * @Description //TODO 查询本周流水
     * @Date 20:24 2019/12/30
     **/
    List<AccountChangeLogBean> getWeekChange(Long userId);

    /**
     * @Author liubo
     * @Description //TODO 查询用户流水
     * @Date 20:24 2019/12/30
     **/
    List<AccountChangeLogBean> getListByUserId(Long userId);
}
