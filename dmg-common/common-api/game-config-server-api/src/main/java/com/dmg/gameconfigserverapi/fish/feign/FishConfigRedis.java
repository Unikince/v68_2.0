package com.dmg.gameconfigserverapi.fish.feign;

/**
 * 捕鱼redis订阅channel
 */
public interface FishConfigRedis {
    /** 鱼 */
    String FISH = "fish:channel:fish";
    /** 房间配置 */
    String FISH_ROOM = "fish:channel:fish_room";
    /** 返奖率控制 */
    String FISH_CTRL_RETURN_RATE = "fish:channel:fish_ctrl_return_rate";
    /** 库存控制 */
    String FISH_CTRL_STOCK = "fish:channel:fish_ctrl_stock";
    /** 鱼线 */
    String FISH_ROUTE = "fish:channel:fish_route";
    /** 场景 */
    String FISH_SCENCE = "fish:channel:fish_scence";
    /** 刷鱼策略 */
    String FISH_STRATEGY = "fish:channel:fish_strategy";
}
