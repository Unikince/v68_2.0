package com.dmg.doudizhuserver.business.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class GenerateSequenceUtil {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public synchronized int generateUniquId() {
        ValueOperations<String, String> v = this.redisTemplate.opsForValue();
        String vv = v.get("ddz_table_number");
        if (vv == null) {
            vv = "1";
        }
        int value = Integer.parseInt(vv);
        this.redisTemplate.opsForValue().set("ddz_table_number", "" + (value + 1));
        return value;
    }
}
