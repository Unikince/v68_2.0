package com.dmg.bairenzhajinhuaserver.service.cache;

import java.util.List;
import java.util.Map;

/**
 * @Description 系统配置
 * @Author mice
 * @Date 2019/7/3 10:19
 * @Version V1.0
 **/
public interface SysConfigService {

    /**
     * @description: 房间配置信息
     * @param
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Integer>>
     * @author mice
     * @date 2019/7/3
    */
    List<Map<String,Integer>> getZJHRoomConfig();
}