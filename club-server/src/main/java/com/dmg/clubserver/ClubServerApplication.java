package com.dmg.clubserver; /**
 * 
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author linanjun 推送服务
 */
@SpringBootApplication
@EnableHystrix
@EnableScheduling
public class ClubServerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ClubServerApplication.class, args);
	}
	
}
