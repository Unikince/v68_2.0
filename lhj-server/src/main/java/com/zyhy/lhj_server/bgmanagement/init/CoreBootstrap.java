package com.zyhy.lhj_server.bgmanagement.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zyhy.lhj_server.bgmanagement.manager.WorkManager;
import com.zyhy.lhj_server.bgmanagement.service.imp.BgManagementServiceImp;
import com.zyhy.lhj_server.bgmanagement.work.QueueType;
import com.zyhy.lhj_server.game.tgpd.TgpdPoolManager;
import com.zyhy.lhj_server.manager.pool.PoolManager;
import com.zyhy.lhj_server.manager.timerWork.manager.TimerManager;

@Component
public class CoreBootstrap {
	@Autowired
	BgManagementServiceImp bgManagementServiceImp;
	@Autowired
	TimerManager timerManager;
	@Autowired
	CreateTable createTable;
	@Autowired
	PoolManager poolManager;
	@Autowired
	TgpdPoolManager tgpdPoolManager;
	
	public void init() throws Exception{
		WorkManager.instance().init(QueueType.values());
		//createTable.init();
		bgManagementServiceImp.init();
		timerManager.init();
		//poolManager.postconstruct();
		//tgpdPoolManager.postconstruct();
	}
}
