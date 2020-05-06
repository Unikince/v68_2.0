package com.dmg.niuniuserver.service.impl;

import com.dmg.niuniuserver.config.RedisKey;
import com.dmg.niuniuserver.service.IdGeneratorService;
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
            RedisAtomicInteger counter = new RedisAtomicInteger(RedisKey.NN_ROOM_ID, stringRedisTemplate.getConnectionFactory());
            return counter.incrementAndGet();
        }
    }


    public Integer getGameNum() {
        synchronized (this){
            RedisAtomicInteger counter = new RedisAtomicInteger(RedisKey.NN_GAME_NUM, stringRedisTemplate.getConnectionFactory());
            return counter.incrementAndGet();
        }
    }
}