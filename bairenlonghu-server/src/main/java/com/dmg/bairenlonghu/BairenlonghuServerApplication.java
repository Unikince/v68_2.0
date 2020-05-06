package com.dmg.bairenlonghu;

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
public class BairenlonghuServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BairenlonghuServerApplication.class, args);
    }

}
