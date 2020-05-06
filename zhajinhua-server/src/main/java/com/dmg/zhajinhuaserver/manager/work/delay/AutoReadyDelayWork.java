package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.service.ReadyService;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoReadyDelayWork extends DelayTimeWork {

	private int roomId;
	private Seat seat;

	@SuppressWarnings("unchecked")
	@Override
	public void init(Object... args) {
		this.roomId = (int) args[0];
		this.seat = (Seat) args[1];
	}

	@Override
	public void go() {
		GameRoom room = RoomManager.instance().getRoom(roomId);
		if (room == null) {
			log.warn("this room is no exist!!");
			return;
		}
		ReadyService readyService = SpringContextUtil.getBean(ReadyService.class);
		if(seat.getPlayer().isActive()) {
			readyService.ready(seat.getPlayer(), true);
		}
	}
}
