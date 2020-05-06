/**
 * 注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.dmg.niuniuserver.tcp.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import com.dmg.niuniuserver.constant.Constant;
import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.Room;
import com.dmg.server.common.util.GameOnlineChangeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dmg.niuniuserver.model.bean.Player;
import com.dmg.niuniuserver.platform.service.PlayerService;
import com.dmg.niuniuserver.tcp.manager.LocationManager;
import com.dmg.niuniuserver.tcp.server.MyWebSocket;

import lombok.extern.slf4j.Slf4j;

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
    private PlayerService playerService;

    @Override
    public List<MyWebSocket> getWebSocketAll() {
        List<MyWebSocket> list = new ArrayList<>(userMap.values().size());
        list.addAll(userMap.values());
        return list;
    }

    @Override
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
                    GameOnlineChangeUtils.decOnlineNum(Constant.GAME_ID, room.getLevel());
                }
            }
            Player player = this.playerService.getPlayer(Long.parseLong(appId));
            if (player != null) {
                player.setOnline(false);
                this.playerService.update(player);
            }
        }
    }

    @Override
    public int getPushSize() {
        return userMap.size();
    }
}
