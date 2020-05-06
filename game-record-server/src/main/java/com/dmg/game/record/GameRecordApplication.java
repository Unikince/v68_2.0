package com.dmg.game.record;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableHystrix
@EnableCaching
@MapperScan("com.dmg.game.record.dao")
@ComponentScan(basePackages = {"com.dmg.*"})
@EnableFeignClients(basePackages={"com.dmg"})
public class GameRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameRecordApplication.class, args);
    }

}
