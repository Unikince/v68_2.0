package com.dmg.bairenniuniuserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableHystrix
@EnableCaching
@EnableFeignClients(basePackages={"com.dmg.gameconfigserverapi"})
@ComponentScan(basePackages = {"com.dmg.*"})
public class BairenniuniuServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BairenniuniuServerApplication.class, args);
    }

}
