package com.dmg.gameconfigserverapi.bjl.feign;

/**
 * 百家乐redis订阅channel
 */
public interface BjlConfigRedis {
    /** 场次配置 */
    String BJL_TABLE = "bjl:channel:bjl_table";
}
