/**
 * 
 */
package com.dmg.lobbyserver.manager.init.impl;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.dmg.lobbyserver.manager.init.ServerInitAction;
import com.dmg.lobbyserver.manager.timer.TimerTaskActionRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author nanjun.li
 * 初始化定时器任务
 */
@Component
public class TimerTaskService implements ServerInitAction {
	// 定时任务
	@Autowired
	private List<TimerTaskActionRun> list;
		
	@Override
	public void initServerAction() {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
		for (TimerTaskActionRun t : list) {
			service.scheduleAtFixedRate(t, t.getInitialDelay(), t.getPeriod(), TimeUnit.MILLISECONDS);
		}
	}
	
	public List<TimerTaskActionRun> getList() {
		return list;
	}
	public void setList(List<TimerTaskActionRun> list) {
		this.list = list;
	}
}
