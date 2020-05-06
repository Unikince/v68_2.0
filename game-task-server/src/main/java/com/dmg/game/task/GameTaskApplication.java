package com.dmg.game.task;

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
@MapperScan("com.dmg.game.task.dao")
@ComponentScan(basePackages = {"com.dmg.*"})
@EnableFeignClients(basePackages={"com.dmg"})
public class GameTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameTaskApplication.class, args);
    }

}
