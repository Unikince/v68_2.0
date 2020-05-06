package com.zyhy.lhj_server;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyhy.lhj_server.bgmanagement.init.CoreBootstrap;


/**
 * @author zhuqd
 * @Date 2017年7月24日
 */
@Service
public class LhjBootstrap {

	@Autowired
	CoreBootstrap bootstrap;

	@PostConstruct
	public void start() {
		CoreBootstrap boot = bootstrap;
		try {
			boot.init();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
