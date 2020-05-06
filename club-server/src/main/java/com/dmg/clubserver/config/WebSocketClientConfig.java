package com.dmg.clubserver.config;

import com.alibaba.fastjson.JSONObject;
import com.dmg.clubserver.process.AbstractMessageHandler;
import com.dmg.clubserver.result.MessageResult;
import com.dmg.clubserver.tcp.manager.LocationManager;
import com.dmg.clubserver.tcp.server.MainProcess;
import com.dmg.clubserver.tcp.server.MyWebSocket;
import com.dmg.clubserver.util.SpringContextUtil;
import com.zyhy.common_server.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.URI;

@Slf4j
@Component
public class WebSocketClientConfig {
    @Value("${lobby_server.ip}")
    private String lobby_server_ip;
    @Value("${lobby_server.socket_port}")
    private String lobby_server_socket_port;

    private static LocationManager locationManager;
    @Autowired
    public void setLocationManager(LocationManager locationManager) {
        WebSocketClientConfig.locationManager = locationManager;
    }

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://"+lobby_server_ip+":"+lobby_server_socket_port), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[websocket] 连接成功");
                }
                @Override
                public void onMessage(String s) {
                	JSONObject jo = JSONObject.parseObject(s);
            		JSONObject data = jo.getJSONObject("data").getJSONObject("data");
            		String messageId = jo.getString("cmd");
                    MainProcess mainProcess = SpringContextUtil.getBean(MainProcess.class);
                    AbstractMessageHandler handler = mainProcess.getHandler(messageId);
            		if(messageId.equals("heart")){
            			log.info("[websocket] 心跳特殊处理");
            			return;
            		}
            		if (handler == null) {
            			log.info("[websocket] 消息号未找到,messageId=" + messageId);
//            			MessageResult result = new MessageResult(1, "消息号未找到,messageId=" + messageId,"");
//            			sendMessage(JsonUtils.object2json(result));
            		}else {
            			MessageResult result = new MessageResult(messageId);
            			Integer appid = data.getInteger("identify");
            			handler.messageHandler(appid+"", data,result);
	                    MyWebSocket webSocket = locationManager.getWebSocket(appid);
	                    if(webSocket != null){
	                    	webSocket.sendMessage(JsonUtils.object2json(result));
	                    }
            		}
                }


                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[websocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    log.info("[websocket] 连接错误={}", ex.getMessage());
                }
            };
            webSocketClient.connect();
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}