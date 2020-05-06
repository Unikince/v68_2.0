package com.dmg.common.starter.rocketmq.common.enums;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:50 2019/12/27
 */
public enum ConsumeMode {
    /**
     * 同时接收异步传递的消息
     */
    CONCURRENTLY,

    /**
     * 有序接收异步传递的消息。一个队列，一个线程
     */
    ORDERLY
}
