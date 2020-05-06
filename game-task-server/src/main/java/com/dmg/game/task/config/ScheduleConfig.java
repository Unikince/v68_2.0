package com.dmg.game.task.config;

import com.dmg.game.task.common.ScheduledThreadPool;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * @Author liubo
 * @Description  多线程调度//TODO
 * @Date 19:11 2020/3/16
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(ScheduledThreadPool.getInstance().getExecutorService());
    }
}
