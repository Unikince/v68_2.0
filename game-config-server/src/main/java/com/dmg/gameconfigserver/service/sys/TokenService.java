package com.dmg.gameconfigserver.service.sys;

import com.dmg.common.core.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:55 2019/11/8
 */
@Component
public class TokenService {

    private final String tokenKeyPrefix = "SYS_LOGIN_USER_TOKEN:";

    private final int redisTokenExpiresDay = 60;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 创建Token
     *
     * @param userId
     * @return
     */
    public String createToken(Long userId) {
        String userKey = tokenKeyPrefix.concat(String.valueOf(userId));
        Object loginToken = redisUtil.get(userKey);
        if (loginToken != null) {
            //单点登陆 销毁用户上一个登陆token
            destroyToken(String.valueOf(loginToken), userId);
        }
        String token = UUID.randomUUID().toString().replace("-", "");
        String realKey = tokenKeyPrefix.concat(token);

        redisUtil.set(realKey, String.valueOf(userId), redisTokenExpiresDay, TimeUnit.MINUTES);
        redisUtil.set(userKey, token, redisTokenExpiresDay, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 校验Token并返回UserId
     *
     * @param token
     * @return
     */
    public Long checkLoginByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String realKey = tokenKeyPrefix.concat(token);
        Object userId = redisUtil.get(realKey);
        if (userId == null) {
            return null;
        }
        // 校验成功延期Token
        redisUtil.expire(realKey, redisTokenExpiresDay, TimeUnit.MINUTES);
        redisUtil.expire(tokenKeyPrefix.concat(String.valueOf(userId)), redisTokenExpiresDay, TimeUnit.MINUTES);
        return Long.parseLong(userId.toString());
    }

    /**
     * 登出销毁Token
     *
     * @param token
     */
    public void destroyToken(String token, Long userId) {
        Assert.notNull(token, "销毁Token失败：Token为空");
        redisUtil.del(tokenKeyPrefix.concat(token), tokenKeyPrefix.concat(String.valueOf(userId)));
    }

}
