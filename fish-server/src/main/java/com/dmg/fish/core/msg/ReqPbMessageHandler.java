package com.dmg.fish.core.msg;

/**
 * 请求消息处理接口
 */
public interface ReqPbMessageHandler {

    /**
     * 消息处理
     *
     * @param player
     * @param msg
     */
    void action(Object player, com.google.protobuf.Message msg);
}
