package com.dmg.common.starter.rocketmq.config;

import com.dmg.common.starter.rocketmq.annotation.RocketMQMessageListener;
import com.dmg.common.starter.rocketmq.core.listener.DefaultRocketMQListenerContainer;
import com.dmg.common.starter.rocketmq.core.listener.RocketMQListener;
import com.dmg.common.starter.rocketmq.properties.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.StandardEnvironment;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 9:58 2019/12/27
 */
@Slf4j
@Order
@Configuration
@ConditionalOnClass(DefaultMQPushConsumer.class)
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnProperty(prefix = "spring.rocketmq", value = "name-server", matchIfMissing = false)
public class ListenerContainerConfig implements ApplicationContextAware, InitializingBean {

    public static final String PROP_NAMESERVER = "nameServer";
    public static final String PROP_TOPIC = "topic";
    public static final String PROP_CONSUMER_GROUP = "consumerGroup";
    public static final String PROP_CONSUME_MODE = "consumeMode";
    public static final String PROP_CONSUME_THREAD_MAX = "consumeThreadMax";
    public static final String PROP_MESSAGE_MODEL = "messageModel";
    public static final String PROP_TAGS = "tags";
    public static final String PROP_ROCKETMQ_LISTENER = "rocketMQListener";
    public static final String PROP_PULL_THRESHOLD_FOR_TOPIC = "pullThresholdForTopic";
    public static final String PROP_PULL_THRESHOLD_SIZE_FOR_TOPIC = "pullThresholdSizeForTopic";
    public static final String METHOD_DESTROY = "destroy";

    private ConfigurableApplicationContext applicationContext;

    private AtomicLong counter = new AtomicLong(0);

    @Resource
    private StandardEnvironment environment;

    @Resource
    private RocketMQProperties rocketMQProperties;

    public ListenerContainerConfig() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(RocketMQMessageListener.class);

        if (Objects.nonNull(beans)) {
            beans.forEach(this::registerContainer);
        }
    }

    private void registerContainer(String beanName, Object bean) {
        Class<?> clazz = AopUtils.getTargetClass(bean);

        if (!RocketMQListener.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + RocketMQListener.class.getName());
        }

        RocketMQListener rocketMQListener = (RocketMQListener) bean;
        RocketMQMessageListener annotation = clazz.getAnnotation(RocketMQMessageListener.class);
        BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(DefaultRocketMQListenerContainer.class);
        beanBuilder.addPropertyValue(PROP_NAMESERVER, rocketMQProperties.getNameServer());
        beanBuilder.addPropertyValue(PROP_TOPIC, environment.resolvePlaceholders(annotation.topic()));

        beanBuilder.addPropertyValue(PROP_CONSUMER_GROUP,
                environment.resolvePlaceholders(annotation.consumerGroup()));
        beanBuilder.addPropertyValue(PROP_CONSUME_MODE, annotation.consumeMode());
        beanBuilder.addPropertyValue(PROP_CONSUME_THREAD_MAX, annotation.consumeThreadMax());
        beanBuilder.addPropertyValue(PROP_MESSAGE_MODEL, annotation.messageModel());
        beanBuilder.addPropertyValue(PROP_TAGS, annotation.tags());
        beanBuilder.addPropertyValue(PROP_ROCKETMQ_LISTENER, rocketMQListener);
        beanBuilder.addPropertyValue(PROP_PULL_THRESHOLD_FOR_TOPIC, annotation.pullThresholdForTopic());
        beanBuilder.addPropertyValue(PROP_PULL_THRESHOLD_SIZE_FOR_TOPIC, annotation.pullThresholdSizeForTopic());
        beanBuilder.setDestroyMethodName(METHOD_DESTROY);

        String containerBeanName = String.format("%s_%s", DefaultRocketMQListenerContainer.class.getName(),
                counter.incrementAndGet());
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(containerBeanName, beanBuilder.getBeanDefinition());

        DefaultRocketMQListenerContainer container = beanFactory.getBean(containerBeanName,
                DefaultRocketMQListenerContainer.class);

        if (!container.isStarted()) {
            try {
                container.start();
            } catch (Exception e) {
                log.error("启动容器失败. {}", container, e);
                throw new RuntimeException(e);
            }
        }

        log.info("将RocketMQListener注册到容器中, listenerBeanName:{}, containerBeanName:{}", beanName, containerBeanName);
    }
}
