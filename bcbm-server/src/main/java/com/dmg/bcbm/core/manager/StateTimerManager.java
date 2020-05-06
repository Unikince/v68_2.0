package com.dmg.bcbm.core.manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.logic.entity.bcbm.Room;
import com.dmg.bcbm.logic.work.StateTimer;

/**
 * 计时器
 */
public class StateTimerManager  {
	private static StateTimerManager instance = new StateTimerManager();
	private static final Logger LOG = LoggerFactory.getLogger(StateTimerManager.class);
	
	private StateTimerManager() {
	}
	public static StateTimerManager instance() {
		return instance;
	}
	
	/**
	 * 定时器
	 */
	private Map<Integer, StateTimer> timers = new ConcurrentHashMap<>();
	
	public void init() {
		Map<Integer, Room> roomMap = RoomManager.intance().getRoomMap(); // 获取所有房间
		roomMap.forEach((k,v) -> {
			StateTimer timer = new StateTimer(v);
			timers.put(v.getRoomId(), timer);
			WorkManager.instance().submit(timer);
			LOG.info("room {}:  =====> timer start!",v.getRoomId());
		});
	}
	
	/**
	 * 获取指定房间定时器
	 * @return 
	 */
	public StateTimer getTimer(int roomId){
		return this.timers.get(roomId);
	}
	
}
