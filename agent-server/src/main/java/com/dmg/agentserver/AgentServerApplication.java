package com.dmg.agentserver;

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

/**
 * 启动入口
 */
@SpringBootApplication
@EnableHystrix
@EnableCaching
@EnableScheduling
@MapperScan("com.dmg.agentserver.business.dao")
@ComponentScan(basePackages = { "com.dmg.*" })
@EnableFeignClients(basePackages = { "com.dmg" })
public class AgentServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AgentServerApplication.class, args);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
