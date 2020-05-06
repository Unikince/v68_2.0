package com.dmg.dataserver.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dmg.data.common.util.Worker;

@Configuration
public class WorkConfig {
    @Bean
    public Worker worker() {
        return new Worker();
    }
}
