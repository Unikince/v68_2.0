package com.dmg.gameconfigserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

@SpringBootApplication
@EnableHystrix
@EnableCaching
@EnableScheduling
@MapperScan("com.dmg.gameconfigserver.dao")
@ComponentScan(basePackages = { "com.dmg.*" })
@EnableFeignClients(basePackages = { "com.dmg" })
public class GameConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameConfigServerApplication.class, args);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
