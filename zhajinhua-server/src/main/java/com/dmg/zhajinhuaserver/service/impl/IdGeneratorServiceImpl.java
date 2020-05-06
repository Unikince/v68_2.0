package com.dmg.zhajinhuaserver.service.impl;

import com.dmg.zhajinhuaserver.config.RedisKey;
import com.dmg.zhajinhuaserver.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @return
 * @author mice
 * @date 2019/7/3
*/
@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Integer getRoomId() {
        synchronized (this){
            RedisAtomicInteger counter = new RedisAtomicInteger(RedisKey.ZJH_ROOM_ID, stringRedisTemplate.getConnectionFactory());
            return counter.incrementAndGet();
        }
    }

    public Integer getGameNum() {
        synchronized (this){
            RedisAtomicInteger counter = new RedisAtomicInteger(RedisKey.ZJH_GAME_NUM, stringRedisTemplate.getConnectionFactory());
            return counter.incrementAndGet();
        }
    }
}