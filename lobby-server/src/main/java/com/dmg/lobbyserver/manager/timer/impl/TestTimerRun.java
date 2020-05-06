/**
 * 
 */
package com.dmg.lobbyserver.manager.timer.impl;

import com.dmg.lobbyserver.manager.timer.TimerTaskActionRun;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author nanjun.li
 * 测试定时任务
 */
@Component
@Order
public class TestTimerRun extends TimerTaskActionRun {

	
	@Override
	public void timerAction() {

	}

}
