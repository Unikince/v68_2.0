package com.dmg.lobbyserver.manager.init.impl;

import com.dmg.lobbyserver.config.RedisKey;
import com.dmg.lobbyserver.dao.UserDao;
import com.dmg.lobbyserver.manager.init.ServerInitAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description 初始化userCode
 * @Author mice
 * @Date 2019/6/18 10:50
 * @Version V1.0
 **/
@Component
public class InitUserCodeService implements ServerInitAction {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserDao userDao;

    @Override
    public void initServerAction() {
        Long userCode = userDao.selectMaxUserCode();
        if (userCode == null || userCode <=0){
            stringRedisTemplate.opsForValue().set(RedisKey.USER_CODE,"100000");
        }else {
            stringRedisTemplate.opsForValue().set(RedisKey.USER_CODE,userCode+1+"");
        }

    }
}