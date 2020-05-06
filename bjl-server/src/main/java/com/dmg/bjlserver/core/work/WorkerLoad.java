package com.dmg.bjlserver.core.work;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WorkerLoad {
    @Bean
    public Worker worker() {
        return new Worker("worker");
    }

    @Bean
    public WorkerGroup workerGroup() {
        return new WorkerGroup("workerGroup");
    }
}
