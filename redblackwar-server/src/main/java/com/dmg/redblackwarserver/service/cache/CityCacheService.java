package com.dmg.redblackwarserver.service.cache;

import com.dmg.redblackwarserver.sysconfig.RegionConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/7 18:47
 * @Version V1.0
 **/
@Service
public interface CityCacheService {

    /**
     * @description: 查询ip : 城市
     * @param ip
     * @return java.lang.String
     * @author mice
     * @date 2019/8/7
    */
    @Cacheable(cacheNames = RegionConfig.CITY, key = "#ip",unless="#result == null")
    String getCity(String ip);

    @CachePut(cacheNames = RegionConfig.CITY, key = "#ip",unless="#result == null")
    String saveCity(String ip, String city);
}