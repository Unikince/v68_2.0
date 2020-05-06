/**
 * 
 */
package com.zyhy.common_lhj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zyhy.common_lhj.context.SpringContext;
import com.zyhy.common_lhj.pool.JackPoolManager;
import com.zyhy.common_lhj.services.UserService;

/**
 * @author ASUS
 *
 */
@Configuration
public class LhjAutoStartConfiguration {

	@Bean
	public SpringContext useContext(){
		return new SpringContext();
	}
	
	@Bean
	public JackPoolManager useJackPoolManager(){
		return new JackPoolManager();
	}
	
	@Bean
	public UserService useUserService(){
		return new UserService();
	}
}
