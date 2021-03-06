/**
 * 注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.dmg.bairenzhajinhuaserver.tcp.server;

import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullListAsEmpty;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteNullStringAsEmpty;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dmg.bairenzhajinhuaserver.common.exception.ScoketBusinessException;
import com.dmg.bairenzhajinhuaserver.common.model.BasePlayer;
import com.dmg.bairenzhajinhuaserver.common.result.MessageResult;
import com.dmg.bairenzhajinhuaserver.common.result.ResultEnum;
import com.dmg.bairenzhajinhuaserver.service.cache.PlayerService;
import com.dmg.bairenzhajinhuaserver.tcp.manager.LocationManager;

import lombok.extern.slf4j.Slf4j;

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

    private static PlayerService playerCacheService;

    @Autowired
    public void setPlayerCacheService(PlayerService playerCacheService) {
        MyWebSocket.playerCacheService = playerCacheService;
    }

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

    /* ---------end---------- */

    // 0当前定位(字符串-9527默认只连接了服务器)
    private String location = "-9527";

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private String appId;

    /**
     * appid 用户id 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("appId") String appId, Session session) {
        log.info("有新连接到来：" + appId);
        this.session = session;
        this.appId = appId;
        MyWebSocket socket = locationManager.getWebSocket(Integer.valueOf(appId));
        if (socket != null) {
            MessageResult messageResult = new MessageResult(MessageIdConfig.NEW_PLAYER_LOGIN, new Object());
            socket.sendMessage(JSON.toJSONString(messageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect));
        }
        locationManager.addUserMap(appId, this);
        BasePlayer player = playerCacheService.getPlayerPlatform(Integer.parseInt(appId));
        if (player != null) {
            player.setOnline(true);
            playerCacheService.updatePlayer(player);
        }
        MessageResult res = new MessageResult(1, "连接成功", MessageIdConfig.CONNECT_SUCCESS);
        this.sendMessage(JSON.toJSONString(res));
        log.info("连接加入! 当前在线人数为" + locationManager.getPushSize());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        locationManager.close(this.appId, this.session);
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
        JSONObject jo = JSON.parseObject(message);
        JSONObject data = jo.getJSONObject("data");
        String messageId = jo.getString("m");
        AbstractMessageHandler handler = mainProcess.getHandler(messageId);
        if (handler == null) {
            MessageResult result = new MessageResult(1, "消息号未找到,messageId=" + messageId, "");
            this.sendMessage(JSON.toJSONString(result));
        } else {
            handler.messageHandler(Integer.parseInt(this.appId), data);
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
        this.sendMessage(result);

    }

    public void sendMessage(String message) {
        synchronized(session){
            try {
                if (this.session.isOpen()) {
                    this.session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    public Session getSession() {
        return this.session;
    }
}
