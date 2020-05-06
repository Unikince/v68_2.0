package com.dmg.niuniuserver.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/7/2 20:47
 * @Version V1.0
 **/
public class RobotConfig {

    // 机器人配置信息
    public static Map<Integer,Integer> robotPercent = new HashMap<>();

    static {
        robotPercent.put(1,20);
        robotPercent.put(2,50);
        robotPercent.put(3,80);
        robotPercent.put(4,100);
        robotPercent.put(5,80);
        robotPercent.put(6,60);
        robotPercent.put(7,40);
        robotPercent.put(8,10);
        robotPercent.put(9,5);
    }
}