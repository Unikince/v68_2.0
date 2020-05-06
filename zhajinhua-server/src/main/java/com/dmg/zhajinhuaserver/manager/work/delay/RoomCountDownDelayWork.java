package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.config.Config;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.model.bean.Seat;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.service.PushService;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.zyhy.common_server.work.DelayTimeWork;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *@author hexf
 * @Date 2018年3月13日
 * @Desc 房间倒计时
 */
public class RoomCountDownDelayWork extends DelayTimeWork {

	private int roomId;

	@Override
	public void init(Object... args) {
		this.roomId = (int) args[0];
	}

	@Override
	public void go() {

		GameRoom room = (GameRoom) RoomManager.instance().getRoom(roomId);

		if (room == null) {
			return;
		}
		if (room.getRoomStatus() == Config.RoomStatus.GAME) {
			return;
		}
		//
		int ready = 0;
		for (Entry<Integer, Seat> entry : room.getSeatMap().entrySet()) {
			Seat data = entry.getValue();
			if (data != null) {
				if (data.isReady()) {
					ready++;
				}
			}
		}
		if (ready > 0) {
			return;
		}
		PushService pushService = SpringContextUtil.getBean(PushService.class);
		RoomService roomService = SpringContextUtil.getBean(RoomService.class);
		roomService.handRoomEnd(room, true);
		Map<String,Object> resultMap = new HashMap<>( );
		resultMap.put("dissolve",true);
		MessageResult messageResult = new MessageResult("132020",resultMap);
		pushService.broadcast(messageResult,room);
	}
}
