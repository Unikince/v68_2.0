/**
 * 
 */
package com.dmg.clubserver.manager.timer.impl;

import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dmg.clubserver.manager.timer.TimerTaskActionRun;


/**
 * @author nanjun.li
 * 测试定时任务
 */
@Component
@Order
public class TestTimerRun extends TimerTaskActionRun {
	@Autowired
	WebSocketClient webSocketClient;
	public TestTimerRun() {
		setPeriod(10000);
	}
	
	@Override
	public void timerAction() {
		if(webSocketClient.isOpen()){
			webSocketClient.send("{'cmd':'heart'}");
		}else{
			try {
				webSocketClient.reconnectBlocking();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
