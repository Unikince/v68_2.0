package com.dmg.clubserver.service;

import com.dmg.clubserver.config.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Description
 * @Author mice
 * @Date 2019/5/27 18:50
 * @Version V1.0
 **/
@Service
public class ValidateCodeService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public boolean timeOut(String phoneNumber){
        String code = stringRedisTemplate.opsForValue().get(RedisKey.VALIDATE_CODE+phoneNumber);
        if (StringUtils.isEmpty(code)){
            return false;
        }
        String[] validateCodeAndTime = code.split("_");

        Long generateTime = Long.parseLong(validateCodeAndTime[1]);
        if (generateTime.compareTo(System.currentTimeMillis()-1000*60*5)<0){
            return false;
        }
        return true;
    }

    public boolean validateSuccess(String phoneNumber,String validateCode){
        String code = stringRedisTemplate.opsForValue().get(RedisKey.VALIDATE_CODE+phoneNumber);
        if (StringUtils.isEmpty(code)){
            return false;
        }
        String[] validateCodeAndTime = code.split("_");
        String validateCode2 =validateCodeAndTime[0];
        if (!validateCode.equals(validateCode2)){
            return false;
        }
        stringRedisTemplate.delete(RedisKey.VALIDATE_CODE+phoneNumber);
        return true;
    }
}