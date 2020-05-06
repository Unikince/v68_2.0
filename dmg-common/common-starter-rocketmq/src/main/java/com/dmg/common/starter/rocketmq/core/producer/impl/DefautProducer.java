package com.dmg.common.starter.rocketmq.core.producer.impl;

import com.dmg.common.starter.rocketmq.common.message.MessageBuild;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.dmg.common.starter.rocketmq.core.producer.MQProducer;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 18:15 2019/11/19
 */
@Slf4j
@Component
public class DefautProducer implements MQProducer, InitializingBean, DisposableBean {

    @Autowired(required = false)
    private DefaultMQProducer producer;

    @Autowired
    @Qualifier("defaultSendCallback")
    private SendCallback defaultSendCallback;

    @Override
    public boolean send(String data) throws Exception {
        Message msg = MessageBuild.build("GAME_RECORD", "GAME_RECORD", data);
        return this.send(msg);
    }

    @Override
    public boolean send(String topic, String tags, String data, long timeout) throws Exception {
        Message msg = MessageBuild.build(topic, tags, data);
        return this.send(msg, timeout);
    }

    @Override
    public boolean send(String topic, String tags, String data) throws Exception {
        Message msg = MessageBuild.build(topic, tags, data);
        return this.send(msg);
    }

    @Override
    public void sendAsync(String data) throws Exception {
        Message msg = MessageBuild.build("GAME_RECORD", "GAME_RECORD", data);
        this.sendAsync(msg);
    }

    @Override
    public void sendAsync(String data, SendCallback sendCallback) throws Exception {
        Message msg = MessageBuild.build("GAME_RECORD", "GAME_RECORD", data);
        this.sendAsync(msg, sendCallback);
    }

    @Override
    public void sendAsync(String topic, String tags, String data, SendCallback sendCallback) throws Exception {
        Message msg = MessageBuild.build(topic, tags, data);
        this.sendAsync(msg, sendCallback);
    }

    @Override
    public void sendAsync(String topic, String tags, String data, long timeout, SendCallback sendCallback) throws Exception {
        Message msg = MessageBuild.build(topic, tags, data);
        this.sendAsync(msg, sendCallback, timeout);
    }

    private Boolean send(Message msg) throws Exception {
        SendResult result = producer.send(msg);
        return result.getSendStatus() == SendStatus.SEND_OK ? true : false;
    }

    private Boolean send(Message msg, long timeout) throws Exception {
        SendResult result = producer.send(msg, timeout);
        return result.getSendStatus() == SendStatus.SEND_OK ? true : false;
    }

    private void sendAsync(Message msg) throws Exception {
        producer.send(msg, defaultSendCallback);
    }

    private void sendAsync(Message msg, SendCallback sendCallback) throws Exception {
        producer.send(msg, sendCallback);
    }

    private void sendAsync(Message msg, SendCallback sendCallback, long timeout) throws Exception {
        producer.send(msg, sendCallback, timeout);
    }

    @Override
    public void destroy() {
        if (null != producer) {
            log.info("rocketMQ producer:{},shutdown!", producer.toString());
            producer.shutdown();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (producer != null && StringUtils.isNotBlank(producer.getProducerGroup())) {
            log.info("rocketMQ producer:{},start!", producer.toString());
            producer.start();
        }
    }
}
