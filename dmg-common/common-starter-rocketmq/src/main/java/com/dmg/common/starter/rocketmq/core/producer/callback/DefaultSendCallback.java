package com.dmg.common.starter.rocketmq.core.producer.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.stereotype.Component;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:41 2019/12/6
 */
@Slf4j
@Component("defaultSendCallback")
public class DefaultSendCallback implements SendCallback {

    @Override
    public void onSuccess(SendResult sendResult) {
        log.info("消息发送成功:{}", sendResult.toString());
    }

    @Override
    public void onException(Throwable throwable) {
        log.error("消息发送失败:{}", throwable.toString());
    }
}
