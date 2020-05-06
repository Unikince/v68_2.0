/**
 * 注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.dmg.zhajinhuaserver.tcp.server;

import com.alibaba.fastjson.JSONObject;

import com.dmg.common.core.util.RedisUtil;
import com.dmg.zhajinhuaserver.exception.ScoketBusinessException;
import com.dmg.zhajinhuaserver.model.bean.Player;
import com.dmg.zhajinhuaserver.process.AbstractMessageHandler;
import com.dmg.zhajinhuaserver.result.MessageResult;
import com.dmg.zhajinhuaserver.result.Result;
import com.dmg.zhajinhuaserver.result.ResultEnum;
import com.dmg.zhajinhuaserver.service.RoomService;
import com.dmg.zhajinhuaserver.service.cache.PlayerService;
import com.dmg.zhajinhuaserver.tcp.manager.LocationManager;
import com.zyhy.common_server.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import static com.dmg.zhajinhuaserver.config.MessageConfig.CONNECT_SUCCESS;

/**
 * @className:--MyWebSocket
 * @author:-----Vito
 * @date:-------2019年3月18日 下午4:16:12
 * @version:----1.0
 * @Description:websocket
 */
@ServerEndpoint(value = "/websocket/{appId}")
@Component
@Slf4j
public class MyWebSocket {

    private static RoomService roomService;

    @Autowired
    public void setRoomService(RoomService roomService) {
        MyWebSocket.roomService = roomService;
    }

    private static PlayerService playerCacheService;

    @Autowired
    public void setPlayerCacheService(PlayerService playerCacheService) {
        MyWebSocket.playerCacheService = playerCacheService;
    }

    /*
     * 以下为解决websocket不能注入问题 ------start----------
     */
    private static LocationManager locationManager;

    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        MyWebSocket.locationManager = locationManager;
    }

    private static MainProcess mainProcess;

    @Autowired
    public void setMainProcess(MainProcess mainProcess) {
        MyWebSocket.mainProcess = mainProcess;
    }

    /*
     * ---------end----------
     */

    // 0当前定位(字符串-9527默认只连接了服务器)
    private String location = "-9527";

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String appId;

    /**
     * appid 用户id
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("appId") String appId, Session session) {
        MyWebSocket myWebSocket = locationManager.getWebSocket(Long.valueOf(appId));
        if (null != myWebSocket) {
            //myWebSocket.sendMessage(MessageResult.error("1002", "用户在别处登录"));
            try {
                myWebSocket.session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("有新连接到来：" + appId);
        this.session = session;
        this.appId = appId;
        try {
            locationManager.addUserMap(appId, this);
        } catch (Exception e) {
            try {
                session.getBasicRemote().sendText(MessageResult.error("1002", "发生错误"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return;
        }
        Player player = playerCacheService.getPlayerPlatform(Long.parseLong(appId));
        if (player != null) {
            player.setActive(true);
            playerCacheService.update(player);
        }
        MessageResult res = new MessageResult(1, "连接成功", CONNECT_SUCCESS);
        sendMessage(JsonUtils.object2json(res));
        log.info("连接加入! 当前在线人数为" + locationManager.getPushSize());

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        locationManager.close(this.appId, session);
        log.info("有一连接关闭！当前在线人数为" + locationManager.getPushSize());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息:" + message);
        JSONObject jo = JSONObject.parseObject(message);
        JSONObject data = jo.getJSONObject("data");
        String messageId = jo.getString("m");
        AbstractMessageHandler handler = mainProcess.getHandler(messageId);
        if (handler == null) {
            MessageResult result = new MessageResult(1, "消息号未找到,messageId=" + messageId, "");
            sendMessage(JsonUtils.object2json(result));
        } else {
            handler.messageHandler(Long.parseLong(appId), data);
        }
    }

    /**
     * 发生错误时调用
     **/
    @OnError
    public void onError(Session session, Throwable exception) {
        exception.printStackTrace();
        String result = "";
        if (exception instanceof ScoketBusinessException) {
            result = MessageResult.error(((ScoketBusinessException) exception).getCmd(), ((ScoketBusinessException) exception).getCode());
        } else {
            result = MessageResult.errorStr(ResultEnum.SYSTEM_EXCEPTION.getCode());

        }
        sendMessage(result);

    }

    public synchronized void sendMessage(String message) {
        try {
            if (null != this.session && this.session.isOpen()) {
                log.info("appid[{}]--->message:{}", appId, message);
                this.session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    public Session getSession() {
        return session;
    }

    public String getAppId(){
        return this.appId;
    }

}
