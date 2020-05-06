package com.dmg.zhajinhuaserver.manager.work.delay;

import com.dmg.zhajinhuaserver.SpringContextUtil;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.GameRoom;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.zyhy.common_server.work.DelayTimeWork;

/**
 * @author hexf
 * @Date 2018年3月13日
 * @Desc 创建房间后10分钟没有开始游戏，自动清除房间
 */
public class NotGameRoomClearDelayWork extends DelayTimeWork {

	private int roomId;

	@Override
	public void init(Object... args) {
		this.roomId = (int) args[0];
	}

	@Override
	public void go() {
		GameRoom room = RoomManager.instance().getRoom(roomId);
		if (room == null) {
			return;
		}
		if (room.getRound() > 0) {
			return;
		}
		RoomService roomService = SpringContextUtil.getBean(RoomService.class);
		roomService.solveRoom(roomId);
	}

}
