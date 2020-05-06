package com.dmg.redblackwarserver.service.cache.impl;

import com.dmg.redblackwarserver.service.cache.CityCacheService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author mice
 * @Date 2019/8/7 15:48
 * @Version V1.0
 **/
@Service
public class CityCacheServiceImpl implements CityCacheService {

    @Override
    public String getCity(String ip) {
        return null;
    }

    @Override
    public String saveCity(String ip, String city) {
        return city;
    }
}