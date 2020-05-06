/**
 * 注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.dmg.zhajinhuaserver.tcp.manager.impl;


import com.dmg.server.common.util.GameOnlineChangeUtils;
import com.dmg.zhajinhuaserver.common.constant.Constant;
import com.dmg.zhajinhuaserver.manager.RoomManager;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.model.bean.Room;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import com.dmg.zhajinhuaserver.tcp.manager.LocationManager;
import com.dmg.zhajinhuaserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Autowired
    private RoomService roomService;

    @Override
    public List<MyWebSocket> getWebSocketAll() {
        List<MyWebSocket> list = new ArrayList<MyWebSocket>(userMap.values().size());
        list.addAll(userMap.values());
        return list;
    }

    public MyWebSocket getWebSocket(Long appid) {
        return userMap.get(appid + "");
    }

    @Override
    public boolean addUserMap(String appId, MyWebSocket mws) {
        userMap.put(appId, mws);
        return true;
    }

    @Override
    public void close(String appId, Session session) {
        MyWebSocket myWebSocket = null;
        if (!StringUtils.isEmpty(appId)) {
            myWebSocket = userMap.get(appId);
        }
        if (myWebSocket != null && myWebSocket.getSession().getId() == session.getId()) {
            log.info("==>移除连接{}", appId);
            userMap.remove(appId);
            Integer roomId = RoomManager.instance().getPlayerRoomIdMap().get(Long.parseLong(appId));
            if (roomId != null) {
                Room room = RoomManager.instance().getRoomMap().get(roomId);
                if (room != null) {
                    GameOnlineChangeUtils.decOnlineNum(Constant.GAME_ID, room.getGrade());
                }
            }
            Player player = playerCacheService.getPlayer(Long.parseLong(appId));
            if (player != null) {
                player.setActive(false);
                playerCacheService.update(player);
            }
            roomService.disconnect(player);
        }
    }

    @Override
    public int getPushSize() {
        return userMap.size();
    }
}
