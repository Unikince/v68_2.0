package com.dmg.data.client.net;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dmg.data.common.constant.NettyErrorEnum;
import com.dmg.data.common.message.NettyMsgException;
import com.dmg.data.common.message.RecvMsg;
import com.dmg.data.common.message.SendMsg;
import com.dmg.data.common.util.SyncUtil;

import io.netty.channel.Channel;

/**
 * 客户端和服务器的连接集合
 */
public class NettyClientMap {
    private static Logger log = LoggerFactory.getLogger(NettyClientMap.class);
    /** 客户端id */
    private static String clientId;
    /** 客户端连接 */
    private static Channel channel;

    /**
     * 发送消息,同步返回
     *
     * @param msgId 请求类型
     * @param data 数据
     */
    public static RecvMsg sendMsg(String msgId, Object data) {
        String unique = UUID.randomUUID().toString();
        RecvMsg recvMsg = null;
        try {
            send(msgId, data, unique);
            String result = SyncUtil.get(unique);
            recvMsg = JSON.parseObject(result, RecvMsg.class);
        } catch (NettyMsgException e) {
            recvMsg = new RecvMsg(msgId, unique, e.getCode(), e.getMessage());
            log.error(e.getMessage(), e);
        } catch (Exception ee) {
            NettyErrorEnum errorEnum = NettyErrorEnum.UNKNOWN;
            NettyMsgException e = new NettyMsgException(errorEnum, ee);
            recvMsg = new RecvMsg(msgId, unique, e.getCode(), e.getMessage());
            log.error(e.getMessage(), e);
        }
        return recvMsg;
    }

    /**
     * 发送消息,无结果
     *
     * @param msgId 请求类型
     * @param data 数据
     */
    public static void sendMsgNotResult(String msgId, Object data) {
        try {
            send(msgId, data, "");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 初始化客户端名
     *
     * @param name 客户端服务名
     */
    protected static void initClientId(String name) {
        if (StringUtils.isBlank(clientId)) {
            clientId = name + ":" + UUID.randomUUID().toString();
        }
    }

    protected static void setChannel(Channel _channel) {
        channel = _channel;
    }

    private static void send(String msgId, Object data, String unique) {
        if (channel != null && channel.isOpen()) {
            SendMsg sendMsg = new SendMsg();
            sendMsg.setClientId(clientId);
            sendMsg.setMsgId(msgId);
            sendMsg.setUnique(unique);
            sendMsg.setData(JSON.toJSONString(data));
            channel.writeAndFlush(JSON.toJSONString(sendMsg) + "\n");
            log.info("客户端发送消息{}", JSON.toJSONString(sendMsg));
        } else {
            throw new NettyMsgException(NettyErrorEnum.SOCKET_ERROR);
        }
    }
}