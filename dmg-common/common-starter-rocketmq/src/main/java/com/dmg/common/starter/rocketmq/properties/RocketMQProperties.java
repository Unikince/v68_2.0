package com.dmg.common.starter.rocketmq.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.rocketmq")
@Data
public class RocketMQProperties {

    private String nameServer;

    private Producer producer = new Producer();

    @Data
    public static class Producer {

        /**
         * 分组
         */
        private String group;

        /**
         * 超时时间
         */
        private int sendMsgTimeout = 3000;

        /**
         * 压缩消息正文阈值，即大于4K的消息体将在默认情况下被压缩
         */
        private int compressMsgBodyOverHowmuch = 1024 * 4;

        /**
         * 同步发送失败后的最大重试次数
         */
        private int retryTimesWhenSendFailed = 2;

        /**
         * 异步发送失败后的最大重试次数
         */
        private int retryTimesWhenSendAsyncFailed = 2;

        /**
         * 发送失败时是否重试其他的broker
         */
        private boolean retryAnotherBrokerWhenNotStoreOk = false;

        /**
         * 允许发送的最大消息
         */
        private int maxMessageSize = 1024 * 1024 * 4;

    }
}
