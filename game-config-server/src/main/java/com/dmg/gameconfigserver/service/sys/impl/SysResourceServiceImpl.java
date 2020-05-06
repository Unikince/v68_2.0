package com.dmg.gameconfigserver.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.sys.SysResourceDao;
import com.dmg.gameconfigserver.model.bean.sys.SysResourceBean;
import com.dmg.gameconfigserver.service.sys.SysResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:46 2019/12/24
 */
@Service
public class SysResourceServiceImpl implements SysResourceService {

    @Autowired
    private SysResourceDao sysResourceDao;

    @Override
    public List<SysResourceBean> getAllList() {
        return sysResourceDao.selectList(new LambdaQueryWrapper<SysResourceBean>().eq(SysResourceBean::getIsDeleted, false));
    }

    @Override
    public List<SysResourceBean> getInfoByUserId(Long userId) {
        return sysResourceDao.getInfoByUserId(userId);
    }

    @Override
    public Boolean getURLIsRelease(Long userId, String url) {
        return sysResourceDao.getCountByUserId(userId, url) > 0;
    }
}
