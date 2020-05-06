package com.zyhy.lhj_server.event.socket;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Component
public class WebSocketClientConfig {
	// V68
	@Value("${lhj.v68_server_host}")
	private String v68_accountHost;
	@Value("${lhj.v68_server_port}")
	private String v68_accountPort;
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketClientConfig.class);

    @Bean
    public WebSocketClient webSocketClient() {
        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://"+v68_accountHost+":"+v68_accountPort+"/websocket/99999996"), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                	LOG.info("[websocket] 连接成功");
                }
                @Override
                public void onMessage(String s) {
                	
                }


                @Override
                public void onClose(int code, String reason, boolean remote) {
                	LOG.info("[websocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    LOG.info("[websocket] 连接错误={}", ex.getMessage());
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