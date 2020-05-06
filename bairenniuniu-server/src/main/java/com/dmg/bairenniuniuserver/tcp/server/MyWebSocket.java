/**
 * 注意：本内容仅限于大拇哥互娱科技有限公司内部传阅，禁止外泄以及用于其他的商业目
 */
package com.dmg.bairenniuniuserver.tcp.server;

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
import com.dmg.bairenniuniuserver.common.exception.ScoketBusinessException;
import com.dmg.bairenniuniuserver.common.model.BasePlayer;
import com.dmg.bairenniuniuserver.common.result.MessageResult;
import com.dmg.bairenniuniuserver.common.result.ResultEnum;
import com.dmg.bairenniuniuserver.service.cache.impl.PlayerServiceImpl;
import com.dmg.bairenniuniuserver.tcp.manager.LocationManager;

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
    private static PlayerServiceImpl playerCacheService;

    @Autowired
    public void setPlayerCacheService(PlayerServiceImpl playerCacheService) {
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
        locationManager.addUserMap(appId, this);
        // InetSocketAddress remoteAddress = WebsocketUtil.getRemoteAddress(session);
        // String city = this.getCity(remoteAddress.getHostString());

        BasePlayer player = playerCacheService.getPlayerPlatform(Integer.parseInt(appId));
        if (player != null) {
            player.setOnline(true);
            // player.setCity(city);
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

    /* public String getCity(String ip) { String city =
     * cityCacheService.getCity(ip); if (StringUtils.isNotEmpty(city)){ return city;
     * } if (StringUtils.isEmpty(ip) ) { return "未知"; } if
     * (ip.startsWith("192.168")) { city = "未知"; cityCacheService.saveCity(ip,city);
     * return city; } String url = "http://api.map.baidu.com/location/ip?ip=" + ip +
     * "&ak=DDlrTGBfKmhzyfZB6nXssTOPrHYCCnqO"; try { String result = ""; URL u = new
     * URL(url); URLConnection uc = u.openConnection();
     * uc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8"); uc.connect();
     * BufferedReader br = new BufferedReader(new
     * InputStreamReader(uc.getInputStream())); String line; while ((line =
     * br.readLine()) != null) { result += "\n" + line; } JSONObject string =
     * JSON.parseObject(result); JSONObject json = string.getJSONObject("content");
     * if (json != null) { JSONObject detail = json.getJSONObject("address_detail");
     * city = detail.getString("city"); cityCacheService.saveCity(ip,city); return
     * city; } } catch (MalformedURLException e) { log.error("Baidu解析ip出错", e);
     * e.printStackTrace(); } catch (IOException e) { log.error("Baidu解析ip出错", e);
     * e.printStackTrace(); } return null; } */
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
