package com.dmg.bjlserver.core.net.socket;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;

import com.dmg.bjlserver.business.platform.model.Player;
import com.dmg.bjlserver.business.platform.service.PlayerService;
import com.dmg.bjlserver.core.msg.MessageMgr;
import com.dmg.bjlserver.core.msg.ReqPbMessageHandler;
import com.google.protobuf.Message;

import lombok.extern.slf4j.Slf4j;

/**
 * Socket服务基础
 */
@Slf4j
public abstract class SocketServer {
    private static PlayerService playerService;
    /** Socket服务管理 */
    protected static SocketMgr socketMgr;
    /** 消息推送线程 */
    protected volatile ScheduledExecutorService pushThread;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        SocketServer.playerService = playerService;
    }

    @Autowired
    public void setSocketMgr(SocketMgr socketMgr) {
        SocketServer.socketMgr = socketMgr;
    }

    /** 消息处理管理器 */
    protected static MessageMgr msgMgr;

    @Autowired
    public void setMessageMgr(MessageMgr msgMgr) {
        SocketServer.msgMgr = msgMgr;
    }

    /** 玩家id */
    protected String playerId;
    /** 客户端连接socket会话 */
    protected Session session;

    /**
     * 连接开始
     *
     * @param playerId 玩家id
     * @param session 客户端连接socket会话
     */
    @OnOpen
    public void onOpen(@PathParam("playerId") String playerId, Session session) {
        session.setMaxIdleTimeout(30000);
        this.playerId = playerId;
        this.session = session;
        this.pushThread = SocketServer.socketMgr.add(playerId, this);
        Player player = SocketServer.playerService.getPlayerPlatform(Long.parseLong(playerId));
        if (player == null) {
            this.closeSession();
        }
        log.info("连接[{}]加入,当前在线人数为[{}]", playerId, SocketServer.socketMgr.size());
    }

    /**
     * 处理消息
     *
     * @param msgId 消息号
     * @param data 消息数据
     */
    public void doMessage(String msgId, com.google.protobuf.Message data) {
        ReqPbMessageHandler handler = msgMgr.getHandler(msgId);
        if (handler == null) {
            log.warn("未知消息ID[{}]", msgId);
        } else {
            handler.action(Long.parseLong(this.playerId), data);
        }
    }

    /**
     * 发生错误时调用
     **/
    @OnError
    public void onError(Throwable cause) {
        if (cause instanceof IOException) {
            log.warn("数据传输IO异常[{}]", cause.getMessage());
        } else {
            log.error("未知错误", cause);
        }
        try {
            this.session.close();
        } catch (IOException e) {
            log.error("onError", e);
        }
    }

    /**
     * 推送消息
     *
     * @param msgId 消息id
     * @param msg 消息数据
     */
    public void pushMsg0(int msgId, Message msg) {
        if (this.pushThread == null) {
            this.closeSession();
        }
        this.pushThread.submit(() -> {
            try {
                this.pushMsg(msgId, msg);
            } catch (Exception e) {
                log.error("消息推送错误,玩家[" + this.playerId + "],消息id[" + msgId + "]", e);
            }
        });
    }

    /**
     * 推送消息
     *
     * @param msgId 消息id
     * @param msg 消息数据
     */
    public abstract void pushMsg(int msgId, Message msg);

    /**
     * 主动关闭session
     */
    public void closeSession() {
        try {
            if (this.session.isOpen()) {
                this.session.close();
            }
        } catch (IOException e) {
            log.error("onError", e);
        }
    }
}
