package com.dmg.lobbyserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dmg.lobbyserver.dao.SysVipPrivilegeConfigDao;
import com.dmg.lobbyserver.dao.bean.SysVipPrivilegeConfigBean;
import com.dmg.server.common.enums.GameType;
import com.dmg.lobbyserver.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.dmg.lobbyserver.config.RedisKey.SYS_CONFIG;


/**
 * @Description 系统配置信息
 * @Author mice
 * @Date 2019/6/25 16:40
 * @Version V1.0
 **/
@Service
public class SysConfigServiceImpl implements SysConfigService {
    @Autowired
    private SysVipPrivilegeConfigDao sysVipPrivilegeConfigDao;

    @Cacheable(cacheNames = SYS_CONFIG, key = "#root.methodName")
    public List<SysVipPrivilegeConfigBean> getSysVipPrivilegeConfig() {
        return sysVipPrivilegeConfigDao.selectList(new LambdaQueryWrapper<SysVipPrivilegeConfigBean>().orderByAsc(SysVipPrivilegeConfigBean::getId));
    }

    @Cacheable(cacheNames = SYS_CONFIG, key = "#root.methodName+'_'+#gameType+'_'+#vipLevel")
    public SysVipPrivilegeConfigBean getGameVipLeveConfig(int vipLevel,int gameType) {
        SysVipPrivilegeConfigBean sysVipPrivilegeConfigBean = sysVipPrivilegeConfigDao.selectOne(new LambdaQueryWrapper<SysVipPrivilegeConfigBean>()
                .eq(SysVipPrivilegeConfigBean::getVipLevel, vipLevel)
                .eq(SysVipPrivilegeConfigBean::getGameType, gameType));
        return sysVipPrivilegeConfigBean;
    }

    @Cacheable(cacheNames = SYS_CONFIG, key = "#root.methodName")
    public Map<Integer, List<Double>> getVIPWashCodeRatioMap() {
        Map<Integer,List<Double>> washCodeRatioMap = new HashMap<>();
        Arrays.stream(GameType.values()).forEach(gameType -> {
            List<SysVipPrivilegeConfigBean> sysVipPrivilegeConfigBeans = sysVipPrivilegeConfigDao.selectList(new LambdaQueryWrapper<SysVipPrivilegeConfigBean>()
                    .eq(SysVipPrivilegeConfigBean::getGameType,gameType.getKey())
                    .orderByAsc(SysVipPrivilegeConfigBean::getVipLevel));
            washCodeRatioMap.put(gameType.getKey(),new ArrayList<>());
            sysVipPrivilegeConfigBeans.forEach(sysVipPrivilegeConfigBean -> {
                washCodeRatioMap.get(gameType.getKey()).add(sysVipPrivilegeConfigBean.getWashCodeRatio());
            });
        });

        return washCodeRatioMap;
    }
}