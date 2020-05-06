package com.dmg.redblackwarserver.service.cache.impl;

import com.dmg.redblackwarserver.service.cache.SysConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.dmg.redblackwarserver.sysconfig.RegionConfig.SYS_CONFIG;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/29 10:22
 * @Version V1.0
 **/
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Cacheable(cacheNames = SYS_CONFIG,key = "#root.methodName")
    public List<Map<String, Integer>> getZJHRoomConfig() {
        List<Map<String, Integer>> roomConfigList = new ArrayList<>();
        // TODO
        return roomConfigList;
    }
}