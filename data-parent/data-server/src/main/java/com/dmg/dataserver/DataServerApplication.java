package com.dmg.dataserver;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringCloudApplication
@EnableFeignClients(basePackages = { "com.dmg.gameconfigserverapi" })
@ComponentScan("com.dmg.*")
public class DataServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataServerApplication.class, args);
    }
}
