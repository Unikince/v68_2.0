package com.dmg.lobbyserver.service;

import com.dmg.lobbyserver.dao.bean.SysVipPrivilegeConfigBean;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/25 16:38
 * @Version V1.0
 **/
public interface SysConfigService {

    /**
     * @description: 获取VIP优惠配置列表
     * @param
     * @return java.util.List<com.dmg.lobbyserver.dao.bean.SysVipPrivilegeConfigBean>
     * @author mice
     * @date 2019/6/25
    */
    List<SysVipPrivilegeConfigBean> getSysVipPrivilegeConfig();

    /**
     * @description: 获取游戏vip相应等级配置
     * @param vipLevel
     * @param gameType
     * @return com.dmg.lobbyserver.dao.bean.SysVipPrivilegeConfigBean
     * @author mice
     * @date 2019/6/28
    */
    SysVipPrivilegeConfigBean getGameVipLeveConfig(int vipLevel,int gameType);

    /**
     * @description: vip戏码配置
     * @param
     * @return java.util.Map<java.lang.Integer,java.util.List<java.lang.Double>>
     * @author mice
     * @date 2019/6/25
    */
    Map<Integer,List<Double>> getVIPWashCodeRatioMap();

}