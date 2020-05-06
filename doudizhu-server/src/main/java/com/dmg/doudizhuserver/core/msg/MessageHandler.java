/**
 *
 */
package com.dmg.doudizhuserver.core.msg;

import com.alibaba.fastjson.JSONObject;

/**
 * 消息处理器通用继承接口
 */
public interface MessageHandler {

    /**
     * 消息处理
     *
     * @param player 玩家id
     * @param params 参数
     */
    void action(long playerId, JSONObject params);
}
