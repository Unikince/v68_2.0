package com.dmg.gameconfigserver.service.api.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.gameconfigserver.dao.config.ResourceServerConfigDao;
import com.dmg.gameconfigserver.model.bean.ResourceServerConfigBean;
import com.dmg.gameconfigserver.service.api.ResourceServerApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description
 * @Author mice
 * @Date 2019/10/25 10:43
 * @Version V1.0
 **/
@Service
public class ResourceServerApiServiceImpl implements ResourceServerApiService {
    @Autowired
    private ResourceServerConfigDao resourceServerConfigDao;
    @Override
    public List<String> getResourceServerList(Integer deployEnvironment) {
        List<String> resourceServerList = new LinkedList<>();
        List<ResourceServerConfigBean> resourceServerConfigBeans = resourceServerConfigDao.selectList(new LambdaQueryWrapper<ResourceServerConfigBean>()
                .eq(ResourceServerConfigBean::getDeployEnvironment,deployEnvironment)
                .orderByAsc(ResourceServerConfigBean::getServerOrder));
        if (CollectionUtil.isEmpty(resourceServerConfigBeans)){
            return resourceServerList;
        }
        for (ResourceServerConfigBean resourceServerConfigBean : resourceServerConfigBeans){
            resourceServerList.add(resourceServerConfigBean.getResourceServer());
        }
        return resourceServerList;
    }
}