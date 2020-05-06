package com.dmg.data.client.net;

public interface DataPushHandler {
    /**
     * 消息处理
     *
     * @param player 玩家id
     * @param params 参数
     */
    void action(String msg);
}
