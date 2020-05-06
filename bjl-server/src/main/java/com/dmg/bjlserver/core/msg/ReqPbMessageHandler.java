package com.dmg.bjlserver.core.msg;

/**
 * 请求消息处理接口
 *
 * @author Alex
 * @date 2016/7/27 11:16
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
