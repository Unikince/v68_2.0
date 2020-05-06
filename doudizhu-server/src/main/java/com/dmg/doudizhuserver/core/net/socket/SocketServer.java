package com.dmg.doudizhuserver.core.net.socket;

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
import com.dmg.doudizhuserver.core.msg.MessageHandler;
import com.dmg.doudizhuserver.core.msg.MessageMgr;
import com.dmg.doudizhuserver.core.msg.MessageResult;

import lombok.extern.slf4j.Slf4j;

/**
 * Socket服务
 */
@Slf4j
@Component
@ServerEndpoint(value = "/websocket/{playerId}")
public class SocketServer {
    /** Socket服务管理 */
    private static SocketMgr socketMgr;

    @Autowired
    public void setSocketMgr(SocketMgr socketMgr) {
        SocketServer.socketMgr = socketMgr;
    }

    /** 消息处理管理器 */
    private static MessageMgr msgMgr;

    @Autowired
    public void setMessageMgr(MessageMgr msgMgr) {
        SocketServer.msgMgr = msgMgr;
    }

    /** 玩家id */
    private String playerId;
    /** 客户端连接socket会话 */
    private Session session;

    /**
     * 连接开始
     *
     * @param playerId 玩家id
     * @param session 客户端连接socket会话
     */
    @OnOpen
    public void onOpen(@PathParam("playerId") String playerId, Session session) {
        this.playerId = playerId;
        this.session = session;

        SocketServer.socketMgr.add(playerId, this);
        log.info("连接[{}]加入,当前在线人数为[{}]", playerId, SocketServer.socketMgr.size());

        MessageResult ressageResult = new MessageResult("8888", 0, "连接成功");
        String pushData = JSON.toJSONString(ressageResult, WriteNullStringAsEmpty, WriteNullListAsEmpty, DisableCircularReferenceDetect);
        this.pushMsg(pushData);
    }

    /**
     * 收到客户端消息
     *
     * @param msg 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String msg) {
        log.debug("收到客户端[{}]的消息[{}]", this.playerId, msg);
        JSONObject json = JSON.parseObject(msg);
        JSONObject data = json.getJSONObject("data");
        String msgId = json.getString("m");
        MessageHandler handler = SocketServer.msgMgr.getHandler(msgId);
        if (handler == null) {
            log.warn("未知消息ID[{}]", msgId);
        } else {
            handler.action(Long.parseLong(this.playerId), data);
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        if (SocketServer.socketMgr.remove(this.playerId, this)) {
            this.onMessage("{\"m\":\"LostOnline\"}");
            log.info("连接[{}]关闭,当前在线人数为[{}]", this.playerId, SocketServer.socketMgr.size());
        }
    }

    /**
     * 发生错误时调用
     **/
    @OnError
    public void onError(Throwable cause) {
        if (cause instanceof IOException) {
            log.warn("数据传输IO异常", cause);
        } else {
            log.error("未知错误", cause);
        }
        try {
            this.session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 推送消息
     *
     * @param data 消息数据
     */
    public synchronized void pushMsg(String data) {
        try {
            if (this.session.isOpen()) {
                this.session.getBasicRemote().sendText(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主动关闭session
     */
    public void closeSession() {
        try {
            if (this.session.isOpen()) {
                this.session.close();
            }
        } catch (IOException e) {
        }
    }
}
