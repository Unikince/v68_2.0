package com.dmg.bairenlonghu.tcp.manager.impl;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dmg.bairenlonghu.common.model.BasePlayer;
import com.dmg.bairenlonghu.manager.RoomManager;
import com.dmg.bairenlonghu.model.Room;
import com.dmg.bairenlonghu.service.cache.PlayerService;
import com.dmg.bairenlonghu.tcp.manager.LocationManager;
import com.dmg.bairenlonghu.tcp.server.MyWebSocket;

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
    private PlayerService playerCacheService;

    @Override
    public List<MyWebSocket> getWebSocketAll() {
        List<MyWebSocket> list = new ArrayList<>(userMap.values().size());
        list.addAll(userMap.values());
        return list;
    }

    @Override
    public MyWebSocket getWebSocket(int appid) {
        return userMap.get(appid + "");
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
            BasePlayer player = this.playerCacheService.getPlayer(Integer.parseInt(appId));
            if (player != null) {
                player.setOnline(false);
                this.playerCacheService.updatePlayer(player);
                if (player.getRoomId() > 0) {
                    Room room = RoomManager.intance().getRoom(player.getRoomId());
                    for (BasePlayer basePlayer1 : room.getApplyToZhuangPlayerList()) {
                        if (basePlayer1.getUserId() == player.getUserId()) {
                            room.getApplyToZhuangPlayerList().remove(basePlayer1);
                            break;
                        }
                    }
                }

            }
        }
    }

    @Override
    public int getPushSize() {
        return userMap.size();
    }
}
