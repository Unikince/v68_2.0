package com.dmg.server.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.common.core.config.RedisRegionConfig;
import com.dmg.common.core.util.RedisUtil;
import com.dmg.common.core.util.SpringUtil;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 18:58 2020/3/19
 */
@Service
public class GameOnlineChangeUtils {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 减少在线人数
     *
     * @param level
     */
    public static void decOnlineNum(int gameId, int level) {
        SpringUtil.getBean(RedisUtil.class)
                .decr(RedisRegionConfig.ONLINE_NUM_KEY + ":" + gameId + "_" + level, Long.parseLong("1"));
    }

    /**
     * 添加在线人数
     *
     * @param level
     */
    public static void incOnlineNum(int gameId, int level) {
        SpringUtil.getBean(RedisUtil.class)
                .incr(RedisRegionConfig.ONLINE_NUM_KEY + ":" + gameId + "_" + level, Long.parseLong("1"));
    }

    /**
     * 初始化在线人数
     *
     * @param level
     */
    public static void initOnlineNum(int gameId, int level) {
        SpringUtil.getBean(RedisUtil.class)
                .set(RedisRegionConfig.ONLINE_NUM_KEY + ":" + gameId + "_" + level, "0");
    }

    /**
     * 初始化在线人数
     *
     * @param level
     */
    public void initOnlineNum0(int gameId, int level) {
        this.redisUtil.set(RedisRegionConfig.ONLINE_NUM_KEY + ":" + gameId + "_" + level, "0");
    }
}
