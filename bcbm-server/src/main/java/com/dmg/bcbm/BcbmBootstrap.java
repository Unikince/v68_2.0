package com.dmg.bcbm;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.bcbm.core.CoreBootstrap;

/**
 * @author zhuqd
 * @Date 2017年7月24日
 */
@Service
public class BcbmBootstrap {

	@Autowired
	CoreBootstrap bootstrap;

	@PostConstruct
	public void start() {
		CoreBootstrap boot = bootstrap;
		try {
			boot.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
