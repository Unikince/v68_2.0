package com.dmg.niuniuserver.service.cache.impl;

import com.dmg.niuniuserver.config.NiouNiou;
import com.dmg.niuniuserver.constant.RegionConstant;
import com.dmg.niuniuserver.service.cache.SysConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 10:22
 * @Version V1.0
 **/
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Cacheable(cacheNames = RegionConstant.SYS_CONFIG,key = "#root.methodName")
    public List<Map<String, Integer>> getRoomConfig() {
        List<Map<String, Integer>> roomConfigList = new ArrayList<>();
        for(int i = 1 ;i < 5;i++) {
            Map<String, Integer> roomConfig = new HashMap<>();
            roomConfig.put("level", NiouNiou.RoomGrade.getIntValue(i).getGrade());
            roomConfig.put("baseScore", NiouNiou.RoomGrade.getIntValue(i).getBaseScore());
            roomConfig.put("lowerLimitScore", NiouNiou.RoomGrade.getIntValue(i).getLowerLimit());
            roomConfig.put("maxLimitScore", NiouNiou.RoomGrade.getIntValue(i).getUpperLimit());
            roomConfig.put("topBet", NiouNiou.RoomGrade.getIntValue(i).getTotalBet());
            roomConfigList.add(roomConfig);
        }
        return roomConfigList;
    }
}