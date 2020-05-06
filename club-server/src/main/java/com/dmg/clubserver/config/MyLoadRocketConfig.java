/**
 * 
 */
package com.dmg.clubserver.config;

import java.util.List;


/**
 * @author Administrator
 * 服务器自定义RocketMQ
 */
//@Configuration
//@EnableAutoConfiguration
public class MyLoadRocketConfig {

	/*@Value("${apache.rocketmq.namesrvAddr}")
	private String nameServer;
	@Value("${eureka.instance.hostname}")
	private String host;
	@Value("${spring.application.name}")
	private String name;
	@Value("${server.port}")
	private String port;
	// 消费者列表
	@Autowired(required = false)
	private List<DmgMQConsumer> consumerHandlers;
	
	@Bean
	RocketmqTemplate rocketmqTemplate() {
		// 生产者
		DmgMQProducer pushMqProducer = new DefaultProducer(nameServer, Groups.CLUB);
		// 服务唯一名称
		String instanceName = name + ":" + host + ":" + port;
		return new RocketmqTemplate(nameServer, pushMqProducer, consumerHandlers, instanceName);
	}*/
}
