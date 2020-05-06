package com.dmg.redblackwarserver.tcp.manager.impl;

import com.dmg.redblackwarserver.common.model.BasePlayer;
import com.dmg.redblackwarserver.manager.RoomManager;
import com.dmg.redblackwarserver.model.Player;
import com.dmg.redblackwarserver.model.Room;
import com.dmg.redblackwarserver.service.cache.PlayerService;
import com.dmg.redblackwarserver.tcp.manager.LocationManager;
import com.dmg.redblackwarserver.tcp.server.MyWebSocket;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * @className:--WebSocketManagerImpl
 * @author:-----Vito
 * @date:-------2019年3月19日 下午5:56:06
 * @version:----1.0
 * @Description:实现连接管路
 */
@Service
@Slf4j
public class WebSocketManagerImpl implements LocationManager {

	@Autowired
	private PlayerService playerCacheService;
	/*@Autowired
	private RoomService roomService;*/
	@Override
	public List<MyWebSocket> getWebSocketAll() {
		List<MyWebSocket> list = new ArrayList<MyWebSocket>(userMap.values().size());
		list.addAll(userMap.values());
		return list;
	}

	public MyWebSocket getWebSocket(int appid){
		return userMap.get(appid+"");
	}

	@Override
	public boolean addUserMap(String appId, MyWebSocket mws) {
		try {
			userMap.put(appId, mws);
			return true;
		} catch (Exception e) {
			mws.onClose();
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public void close(String appId, Session session) {
		MyWebSocket myWebSocket = userMap.get(appId);
		if (myWebSocket != null && myWebSocket.getSession().getId() == session.getId()) {
			log.info("==>移除连接{}", appId);
			userMap.remove(appId);
			BasePlayer player = playerCacheService.getPlayer(Integer.parseInt(appId));
			if (player != null){
				player.setOnline(false);
				playerCacheService.updatePlayer(player);
				if (player.getRoomId()>0){
					Room room = RoomManager.intance().getRoom(player.getRoomId());
					if (room == null)return;
					GameOnlineChangeUtils.decOnlineNum(Integer.parseInt(RoomManager.intance().getGameId()),room.getLevel());
				}
			}
			//roomService.disconnect(player);
		}
	}
	
	@Override
	public int getPushSize() {
		return userMap.size();
	}
}
