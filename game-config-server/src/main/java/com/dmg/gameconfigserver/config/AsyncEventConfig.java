package com.dmg.gameconfigserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 16:45 2019/12/25
 */
@Configuration
@EnableAsync
public class AsyncEventConfig implements AsyncConfigurer {
}
