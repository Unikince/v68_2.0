package com.dmg.zhajinhuaserver.service.cache.impl;

import com.dmg.zhajinhuaserver.service.cache.SysConfigService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dmg.zhajinhuaserver.common.constant.RegionConstant.SYS_CONFIG;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/3 10:22
 * @Version V1.0
 **/
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Cacheable(cacheNames = SYS_CONFIG,key = "#root.methodName")
    public List<Map<String, Integer>> getZJHRoomConfig() {
        List<Map<String, Integer>> roomConfigList = new ArrayList<>();
        for(int i = 1 ;i < 5;i++) {
            Map<String, Integer> roomConfig = new HashMap<>();
            /*roomConfig.put("level", RoomConfig.RoomGrade.getIntValue(i).getGrade());
            roomConfig.put("baseScore",RoomConfig.RoomGrade.getIntValue(i).getBaseScore());
            roomConfig.put("lowerLimitScore", RoomConfig.RoomGrade.getIntValue(i).getLowerLimit());
            roomConfig.put("maxLimitScore", RoomConfig.RoomGrade.getIntValue(i).getUpperLimit());
            roomConfig.put("topBet", RoomConfig.RoomGrade.getIntValue(i).getTotalBet());
            roomConfigList.add(roomConfig);
             */
        }
        return roomConfigList;
    }
}