package com.dmg.niuniuserver.manager.work;

import com.dmg.niuniuserver.manager.RoomManager;
import com.dmg.niuniuserver.model.bean.GameRoom;
import com.dmg.niuniuserver.tcp.manager.impl.WebSocketManagerImpl;
import com.dmg.niuniuserver.tcp.server.MyWebSocket;
import com.zyhy.common_server.work.DelayTimeWork;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description:
 * @Author: Lemon
 * @Date: 2019/9/5 10:33
 * @Version: 1.0
 */
@Slf4j
public class ServerMaintenanceTimeWork extends DelayTimeWork {

    private WebSocketManagerImpl webSocketManager;

    @Override
    public void init(Object... args) {
        this.webSocketManager = (WebSocketManagerImpl) args[0];
    }

    @Override
    public void go() {
        List<MyWebSocket> webSockets = webSocketManager.getWebSocketAll();
        for (MyWebSocket webSocket : webSockets) {
            webSocket.onClose();
        }
        RoomManager roomManager = RoomManager.instance();
        for (GameRoom value : roomManager.getRoomMap().values()) {
            value.clearAll();
        }
        roomManager.getPlayerRoomIdMap().clear();
        roomManager.getRoomStatusMap().clear();
        roomManager.getRoomRoundMap().clear();
    }
}
