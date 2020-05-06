package com.dmg.common.starter.rocketmq.core.listener;

import com.dmg.common.starter.rocketmq.common.enums.ConsumeMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:50 2019/12/27
 */
@Slf4j
public class DefaultRocketMQListenerContainer implements InitializingBean, DisposableBean {

    @Setter
    @Getter
    private long suspendCurrentQueueTimeMillis = 1000;

    @Setter
    @Getter
    private int delayLevelWhenNextConsume = 0;

    @Setter
    @Getter
    private String consumerGroup;

    @Setter
    @Getter
    private String nameServer;

    @Setter
    @Getter
    private String topic;

    @Setter
    @Getter
    private String tags;

    @Setter
    @Getter
    private ConsumeMode consumeMode = ConsumeMode.CONCURRENTLY;

    @Setter
    @Getter
    private MessageModel messageModel = MessageModel.CLUSTERING;

    @Setter
    @Getter
    private int consumeThreadMax = 64;

    @Setter
    @Getter
    private int pullThresholdForTopic = -1;

    @Setter
    @Getter
    private int pullThresholdSizeForTopic = -1;

    @Getter
    @Setter
    private String charset = "UTF-8";

    @Setter
    @Getter
    private ObjectMapper objectMapper = new ObjectMapper();

    @Setter
    @Getter
    private boolean started;

    @Setter
    private RocketMQListener rocketMQListener;

    private DefaultMQPushConsumer consumer;

    public void setupMessageListener(RocketMQListener rocketMQListener) {
        this.rocketMQListener = rocketMQListener;
    }

    public synchronized void start() throws MQClientException {

        if (this.isStarted()) {
            throw new IllegalStateException("容器已启动. " + this.toString());
        }

        initRocketMQPushConsumer();

        consumer.start();
        this.setStarted(true);

        log.info("启动容器: {}", this.toString());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
    }

    @Override
    public void destroy() {
        this.setStarted(false);
        if (Objects.nonNull(consumer)) {
            consumer.shutdown();
        }
        log.info("销毁容器, {}", this.toString());
    }

    @Override
    public String toString() {
        return "DefaultRocketMQListenerContainer{" + "consumerGroup='" + consumerGroup + '\'' + ", nameServer='"
                + nameServer + '\'' + ", topic='" + topic + '\'' + ", consumeMode=" + consumeMode + ", messageModel="
                + messageModel + '}';
    }

    private void initRocketMQPushConsumer() throws MQClientException {

        Assert.notNull(rocketMQListener, "rocketMQListener不能为null");
        Assert.notNull(consumerGroup, "consumerGroup不能为null");
        Assert.notNull(nameServer, "nameServer不能为null");
        Assert.notNull(topic, "topic不能为null");

        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(nameServer);
        consumer.setConsumeThreadMax(consumeThreadMax);
        if (consumeThreadMax < consumer.getConsumeThreadMin()) {
            consumer.setConsumeThreadMin(consumeThreadMax);
        }
        consumer.setPullThresholdForTopic(pullThresholdForTopic);
        consumer.setPullThresholdSizeForTopic(pullThresholdSizeForTopic);
        consumer.setMessageModel(messageModel);
        consumer.subscribe(topic, tags);

        switch (consumeMode) {
            case ORDERLY:
                consumer.setMessageListener(new DefaultMessageListenerOrderly());
                break;
            case CONCURRENTLY:
                consumer.setMessageListener(new DefaultMessageListenerConcurrently());
                break;
            default:
                throw new IllegalArgumentException("属性“consumeMode”错误.");
        }

    }

    public class DefaultMessageListenerConcurrently implements MessageListenerConcurrently {
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            for (MessageExt messageExt : msgs) {
                try {
                    long now = System.currentTimeMillis();
                    rocketMQListener.onMessage(messageExt);
                    long costTime = System.currentTimeMillis() - now;
                    log.info("消费消息成功. msg：{}, 耗时: {} ms", new String(messageExt.getBody(), "utf-8"), costTime);
                } catch (Exception e) {
                    log.error("消费消息失败. msg: {},{}", messageExt, e);
                    context.setDelayLevelWhenNextConsume(delayLevelWhenNextConsume);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public class DefaultMessageListenerOrderly implements MessageListenerOrderly {
        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            for (MessageExt messageExt : msgs) {
                try {
                    long now = System.currentTimeMillis();
                    rocketMQListener.onMessage(messageExt);
                    long costTime = System.currentTimeMillis() - now;
                    log.info("消费消息成功. msg：{}, 耗时: {} ms", new String(messageExt.getBody(), "utf-8"), costTime);
                } catch (Exception e) {
                    log.error("消费消息失败. msg:{},{} ", messageExt, e);
                    context.setSuspendCurrentQueueTimeMillis(suspendCurrentQueueTimeMillis);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }

            return ConsumeOrderlyStatus.SUCCESS;
        }
    }

}
