package com.zyhy.lhj_server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import com.zyhy.common_lhj.process.IMsgProcess;
import com.zyhy.common_lhj.process.MainProcess;

/**
 * Hello world!
 *
 */
@SpringCloudApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EnableAutoConfiguration
@EnableFeignClients(basePackages={"com.zyhy.lhj_server"})
public class LhjApplication 
{
	public static void main(String[] args) {
		SpringApplication.run(LhjApplication.class, args);
	}
	
	@Autowired
	private List<IMsgProcess> list;
	
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate;
	}
	
	@Bean
	public MainProcess initServerAction() {
		MainProcess mainProcess = new MainProcess();
		Map<Integer, IMsgProcess> map = new HashMap<Integer, IMsgProcess>();
		for (IMsgProcess i : list) {
			map.put(i.getMessageId(), i);
		}
		mainProcess.setHttpHandlerMapping(map);
		return mainProcess;
	}
}
