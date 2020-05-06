/**
 * 
 */
package com.dmg.niuniuserver.manager.timer;

/**
 * @author nanjun.li
 *
 */
public interface TimerAction {

	void timerAction();
	
	// 延迟间隔时间(毫秒)
	long getInitialDelay();
	
	//  执行间隔时间(毫秒)
	long getPeriod();
}
