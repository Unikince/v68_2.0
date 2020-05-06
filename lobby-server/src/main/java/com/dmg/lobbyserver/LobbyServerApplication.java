package com.dmg.lobbyserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * v68 大厅服务
 */
@SpringBootApplication
@EnableHystrix
@EnableScheduling
@EnableCaching
@ComponentScan(basePackages = {"com.dmg.*"})
@EnableFeignClients(basePackages={"com.dmg"})
public class LobbyServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(LobbyServerApplication.class, args);
	}
	
}
