package com.dmg.dataserver.common.net.socket;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dmg.data.common.message.RecvMsg;

import io.netty.channel.Channel;

public class NettyServerMap {
    private static Logger log = LoggerFactory.getLogger(NettyServerMap.class);
    private static Map<String, Channel> clientMap = new ConcurrentHashMap<>();

    public static boolean isOpenClient(String key) {
        Channel channel = clientMap.get(key);
        if (channel != null && channel.isOpen()) {
            return true;
        } else {
            return false;
        }
    }

    public static Channel getClientChannel(String key) {
        return clientMap.get(key);
    }

    protected static void setMapValue(String clientId, Channel channel) {
        clientMap.put(clientId, channel);
    }

    protected static void removeMapValue(String clientId) {
        clientMap.remove(clientId);
    }

    /**
     * 交互消息的发送
     *
     * @param clientId 客户端id
     * @param recvMsg 发送消息
     */
    public static void send(String clientId, RecvMsg recvMsg) {
        if (recvMsg == null) {
            return;
        }
        if (StringUtils.isBlank(recvMsg.getUnique())) {
            return;
        }
        Channel channel = clientMap.get(clientId);
        if (channel != null && channel.isOpen()) {
            channel.writeAndFlush(JSON.toJSONString(recvMsg) + "\n");
            if (recvMsg.getCode() != 0) {
                log.error("服务端发送到[{}]错误{}", clientId, JSON.toJSONString(recvMsg));
            } else {
                log.info("服务端发送到[{}]消息{}", clientId, JSON.toJSONString(recvMsg));
            }
        }
    }

    /**
     * 服务器主动推送消息到客户端
     *
     * @param clientId 客户端id
     * @param msgId 消息id
     * @param data 推送数据
     */
    public static void push(String clientId, String msgId, Object data) {
        if (StringUtils.isBlank(clientId) || StringUtils.isBlank(msgId)) {
            return;
        }
        RecvMsg pushMsg = new RecvMsg();
        pushMsg.setMsgId(msgId);
        pushMsg.setData(data);
        Channel channel = clientMap.get(clientId);
        if (channel != null && channel.isOpen()) {
            channel.writeAndFlush(JSON.toJSONString(pushMsg) + "\n");
            log.info("服务端推送到[{}]消息{}", clientId, JSON.toJSONString(pushMsg));
        }
    }

    /**
     * 服务器主动推送消息到客户端
     *
     * @param msgId 消息id
     * @param data 推送数据
     */
    public static void pushAll(String msgId, Object data) {
        if (StringUtils.isBlank(msgId)) {
            return;
        }
        RecvMsg pushMsg = new RecvMsg();
        pushMsg.setMsgId(msgId);
        pushMsg.setData(data);
        for (Entry<String, Channel> client : clientMap.entrySet()) {
            if (client.getValue() != null && client.getValue().isOpen()) {
                client.getValue().writeAndFlush(JSON.toJSONString(pushMsg) + "\n");
                log.info("服务端推送到[{}]消息{}", client.getKey(), JSON.toJSONString(pushMsg));
            }
        }
    }
}
