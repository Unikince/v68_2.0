/**
 * 注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.dmg.lobbyserver.tcp.manager.impl;

import com.dmg.lobbyserver.tcp.manager.LocationManager;
import com.dmg.lobbyserver.tcp.server.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
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

    @Override
    public List<MyWebSocket> getWebSocketAll() {
        List<MyWebSocket> list = new ArrayList<>(userMap.values().size());
        list.addAll(userMap.values());
        return list;
    }

    public MyWebSocket getWebSocket(Long appid) {
        return userMap.get(appid + "");
    }

    @Override
    public boolean addUserMap(String appId, MyWebSocket mws) {
        try {
            MyWebSocket zqmws = userMap.get(appId);
            if (zqmws != null) {
                return false;
            } else {
                userMap.put(appId, mws);
                return true;
            }
        } catch (Exception e) {
            mws.onClose();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close(String appId, Session session) {
        MyWebSocket myWebSocket = userMap.get(appId);
        if (myWebSocket != null) {
            userMap.remove(appId);
            log.info("==>移除连接{}", appId);
        }
    }

    @Override
    public int getPushSize() {
        return userMap.size();
    }
}
