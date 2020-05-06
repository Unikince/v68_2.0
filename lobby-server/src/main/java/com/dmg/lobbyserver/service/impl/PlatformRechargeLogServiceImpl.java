package com.dmg.lobbyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.PlatformRechargeLogDao;
import com.dmg.lobbyserver.dao.bean.PlatformRechargeLogBean;
import com.dmg.lobbyserver.service.PlatformRechargeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Description
 * @Author mice
 * @Date 2019/11/26 18:00
 * @Version V1.0
 **/
@Service
public class PlatformRechargeLogServiceImpl implements PlatformRechargeLogService {
    @Autowired
    private PlatformRechargeLogDao platformRechargeLogDao;

    @Override
    public String idempotentValidate(String id) {
        PlatformRechargeLogBean platformRechargeLogBean = platformRechargeLogDao.selectOne(new LambdaQueryWrapper<PlatformRechargeLogBean>().eq(PlatformRechargeLogBean::getThirdOrderId,id));
        if (platformRechargeLogBean == null){
            return null;
        }
        platformRechargeLogBean.setRequestCount(platformRechargeLogBean.getRequestCount()+1);
        platformRechargeLogDao.updateById(platformRechargeLogBean);
        return platformRechargeLogBean.getResponseBody();
    }

    public PlatformRechargeLogBean save(PlatformRechargeLogBean platformRechargeLogBean){
        platformRechargeLogBean.setCreateDate(new Date());
        platformRechargeLogBean.setRequestCount(1);
        platformRechargeLogDao.insert(platformRechargeLogBean);
        return platformRechargeLogBean;
    }

    @Override
    public PlatformRechargeLogBean getByOrderId(String orderId) {
        return platformRechargeLogDao.selectOne(new LambdaQueryWrapper<PlatformRechargeLogBean>().eq(PlatformRechargeLogBean::getOrderId,orderId));
    }

    @Override
    public boolean playerHasRecharge(Long userId) {
        return platformRechargeLogDao.selectPlayerHasRecharge(userId);
    }

    public PlatformRechargeLogBean updateById(PlatformRechargeLogBean platformRechargeLogBean){
        platformRechargeLogDao.updateById(platformRechargeLogBean);
        return platformRechargeLogBean;
    }

}