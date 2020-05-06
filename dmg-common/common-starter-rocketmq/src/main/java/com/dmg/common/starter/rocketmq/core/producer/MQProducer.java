package com.dmg.common.starter.rocketmq.core.producer;

import org.apache.rocketmq.client.producer.SendCallback;

/**
 * @author ASUS
 */
public interface MQProducer {

    default String getGroup() {
        return null;
    }

    /**
     * @Author liubo
     * @Description //TODO 同步消息发送
     * @Date 17:16 2019/11/21
     **/
    default boolean send(String data) throws Exception {
        return false;
    }

    default boolean send(String topic, String tags, String data) throws Exception {
        return false;
    }

    default boolean send(String topic, String tags, String data, long timeout) throws Exception {
        return false;
    }

    /**
     * @Author liubo
     * @Description //TODO 异步发送
     * @Date 17:08 2019/11/21
     **/
    default void sendAsync(String data) throws Exception {
    }

    default void sendAsync(String data, SendCallback sendCallback) throws Exception {
    }

    default void sendAsync(String topic, String tags, String data, SendCallback sendCallback) throws Exception {
    }

    default void sendAsync(String topic, String tags, String data, long timeout, SendCallback sendCallback) throws Exception {
    }


}
