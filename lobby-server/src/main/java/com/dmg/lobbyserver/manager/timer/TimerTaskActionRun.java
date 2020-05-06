/**
 * 
 */
package com.dmg.lobbyserver.manager.timer;

import com.zyhy.common_server.util.DateUtils;

/**
 * @author nanjun.li
 *
 */
public abstract class TimerTaskActionRun implements TimerAction,Runnable{

	// 延迟间隔(毫秒)
	private long initialDelay = 0;
	// 执行间隔(毫秒)
	private long period = DateUtils.ONE_DAY_MS;
		
	@Override
	public void run() {
		try {
			timerAction();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public long getInitialDelay() {
		return initialDelay;
	}
		
	@Override
	public long getPeriod() {
		return period;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public void setPeriod(long period) {
		this.period = period;
	}
}
