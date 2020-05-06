package com.dmg.doudizhuserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.dmg.doudizhuserver.core.work.Worker;

/**
 * 启动入口
 */
@SpringBootApplication
@EnableHystrix
@EnableCaching
@EnableFeignClients
@ComponentScan(basePackages = { "com.dmg" })
public class DoudizhuServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoudizhuServerApplication.class, args);
    }

    @Bean
    public Worker worker() {
        return new Worker("ddz-worker");
    }
}
