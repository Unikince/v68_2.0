package com.dmg.bcbm.logic.work.timer;

import java.util.Map;
import java.util.Map.Entry;

import com.dmg.bcbm.core.abs.role.Role;
import com.dmg.bcbm.core.abs.work.TimeWork;
import com.dmg.bcbm.core.annotation.Cron;
import com.dmg.bcbm.core.manager.RoomManager;
import com.dmg.bcbm.logic.entity.bcbm.Room;

/**
 * @author zhuqd
 * @Date 2017年9月5日
 * @Desc 定时清理长时间掉线玩家。掉线5分钟将玩家从缓存清除
 */
@Cron("15 */1 * * * ?")
public class ClearInactivePlayerTimeWork extends TimeWork {

	@Override
	public void init(Object... args) {
	}

	@Override
	public void go() {
		long now = System.currentTimeMillis();
		Map<Integer, Room> roomMap = RoomManager.intance().getRoomMap();
		for (Room room : roomMap.values()) {
			Map<String, Role> roleMap =room.getRoleMap();
			for (Entry<String, Role> entry : roleMap.entrySet()) {
				Role role = entry.getValue();
				if (!role.isActive() && now - role.getLastActiveTime() > 5 * 60 * 1000) {
					roleMap.remove(role.getRoleId());
					System.out.println("room = " + room.getRoomId() + " =====>" + "玩家 " + role.getRoleId() + " 掉线超过5分钟,清除缓存!");
				}
			}
		}
		
	}

}
