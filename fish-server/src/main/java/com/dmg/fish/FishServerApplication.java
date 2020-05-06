package com.dmg.fish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动入口
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@ComponentScan(basePackages = { "com.dmg" })
@EnableFeignClients(basePackages = { "com.dmg.gameconfigserverapi" })
public class FishServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(FishServerApplication.class, args);
    }
}
