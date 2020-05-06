/**
 * 
 */
package com.dmg.common.starter.rocketmq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ASUS
 *
 */
@Configuration
public class RocketmqAutoStartConfiguration {

	@Bean
	public RocketmqTemplate getRocketmqTemplate() {
		return new RocketmqTemplate();
	}
}
