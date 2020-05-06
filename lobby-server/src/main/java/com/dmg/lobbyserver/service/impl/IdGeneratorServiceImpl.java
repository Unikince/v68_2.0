package com.dmg.lobbyserver.service.impl;

import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.service.IdGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author mice
 * @Date 2019/6/18 10:39
 * @Version V1.0
 **/
@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Long getUserCode() {
        synchronized (this){
            RedisAtomicLong counter = new RedisAtomicLong(RedisKey.USER_CODE, stringRedisTemplate.getConnectionFactory());
            return counter.incrementAndGet();
        }
    }
}