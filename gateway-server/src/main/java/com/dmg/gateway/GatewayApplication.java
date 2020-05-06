package com.dmg.gateway;

import com.dmg.gateway.loadbalance.LoadBalanceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableEurekaClient
@RibbonClients(defaultConfiguration = LoadBalanceConfig.class)
@EnableScheduling
@SpringBootApplication(exclude = {GatewayAutoConfiguration.class})
@ComponentScan("com.dmg.*")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}