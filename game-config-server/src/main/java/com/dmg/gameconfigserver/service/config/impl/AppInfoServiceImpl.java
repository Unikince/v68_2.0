package com.dmg.gameconfigserver.service.config.impl;

import com.dmg.gameconfigserver.dao.app.AppInfoDao;
import com.dmg.gameconfigserver.model.bean.AppInfoBean;
import com.dmg.gameconfigserver.service.config.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("appInfoService")
public class AppInfoServiceImpl implements AppInfoService {

    @Autowired
    private AppInfoDao appInfoDao;


    @Override
    public void updateAppInfo(AppInfoBean appInfoBean) {
        appInfoDao.updateById(appInfoBean);
    }
}
