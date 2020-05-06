package com.dmg.lobbyserver.service.impl;

import com.dmg.lobbyserver.dao.SysRechargeLogDao;
import com.dmg.lobbyserver.dao.bean.SysRechargeLogBean;
import com.dmg.lobbyserver.service.SysRechargeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/28 11:34
 * @Version V1.0
 **/
@Service
public class SysRechargeLogServiceImpl implements SysRechargeLogService {
    @Autowired
    private SysRechargeLogDao sysRechargeLogDao;


    @Override
    public void save(SysRechargeLogBean sysRechargeLogBean) {
        sysRechargeLogBean.setCreatDate(new Date());
        sysRechargeLogDao.insert(sysRechargeLogBean);
    }
}