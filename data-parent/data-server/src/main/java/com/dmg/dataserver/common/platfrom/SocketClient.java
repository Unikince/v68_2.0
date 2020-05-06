package com.dmg.dataserver.common.platfrom;

import java.net.URI;

import javax.annotation.PostConstruct;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SocketClient {
    @Value("${platfrom_server.host}")
    private String serverHost;
    @Value("${platfrom_server.port}")
    private String serverPort;

    private WebSocketClient client;

    @PostConstruct
    public void init() {
        try {
            URI uri = new URI("ws://" + this.serverHost + ":" + this.serverPort + "/websocket/999999994");
            this.client = new WebSocketClient(uri, new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("平台 websocket连接成功");
                }

                @Override
                public void onMessage(String message) {
                    log.info("平台 websocket消息:{}", message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("平台 websocket退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    log.info("平台 websocket连接错误={}", ex.getMessage());
                }
            };
            this.client.connect();
        } catch (Exception e) {
            log.error("平台 websocket初始化错误");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void send(String m, JSONObject data) {
        JSONObject msg = new JSONObject();
        msg.put("m", m);
        msg.put("data", data);

        synchronized (this.client) {
            if (!this.client.isOpen()) {
                try {
                    this.client.reconnectBlocking();
                } catch (InterruptedException e) {
                    log.error("平台 websocket重连失败");
                    e.printStackTrace();
                }
            }
            this.client.send(msg.toJSONString());
        }
    }
}