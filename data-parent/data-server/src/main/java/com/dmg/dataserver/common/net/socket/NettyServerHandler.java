package com.dmg.dataserver.common.net.socket;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dmg.data.common.constant.MsgConst;
import com.dmg.data.common.constant.NettyErrorEnum;
import com.dmg.data.common.message.NettyMsgException;
import com.dmg.data.common.message.RecvMsg;
import com.dmg.data.common.message.SendMsg;
import com.dmg.dataserver.common.net.annotation.ActionMapUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * 服务端处理器
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    /** 日志 */
    private Logger log = LoggerFactory.getLogger(this.getClass());
    /** 心跳 */
    AttributeKey<Integer> COUNTER = AttributeKey.valueOf("COUNTER");
    /** 客户端id */
    AttributeKey<String> CLIENT_ID = AttributeKey.valueOf("CLIENT_ID");

    ThreadPoolExecutor executor = new ThreadPoolExecutor(100, // 初始核心线程
            100, // 最大线程
            30, // 允许线程闲置时间
            TimeUnit.SECONDS, // 允许线程闲置时间单位
            new ArrayBlockingQueue<>(500), // 允许最大阻塞任务
            new ThreadPoolExecutor.CallerRunsPolicy());// 当阻塞任务达到队列长度+最大线程数量时候执行的处理策略(如下是由调用者执行)

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        this.log.info("服务端接收消息:{}", msg);
        SendMsg sendMsg = JSON.parseObject(msg, SendMsg.class);
        String msgId = sendMsg.getMsgId();
        switch (msgId) {
            case MsgConst.LOGIN: {// 登录
                Channel channel = ctx.channel();
                String clientId = sendMsg.getClientId();
                NettyServerMap.setMapValue(clientId, ctx.channel());
                channel.attr(this.CLIENT_ID).set(clientId);
                this.log.info("--------[{}]服务登录！", sendMsg.getClientId());
                break;
            }
            case MsgConst.PING: {// 心跳
                Channel channel = ctx.channel();
                Attribute<Integer> counterKey = channel.attr(this.COUNTER);
                counterKey.set(0);
                break;
            }
            default: {
                String unique = sendMsg.getUnique();
                try {
                    this.executor.execute(() -> {
                        this.log.info("服务端处理消息[{}][{}]", msgId, unique);
                        RecvMsg recvMsg = null;
                        try {
                            recvMsg = (RecvMsg) ActionMapUtil.invote(sendMsg);
                            if (recvMsg != null) {
                                if (StringUtils.isBlank(recvMsg.getMsgId())) {
                                    recvMsg.setMsgId(msgId);
                                }
                                if (StringUtils.isBlank(recvMsg.getUnique())) {
                                    recvMsg.setUnique(unique);
                                }
                            }
                        } catch (Exception ee) {
                            NettyMsgException e = null;
                            if (ee instanceof InvocationTargetException) {
                                Throwable e2= ((InvocationTargetException)ee).getCause();
                                if (e2 instanceof NettyMsgException) {
                                    e = (NettyMsgException) e2;
                                } else {
                                    e = new NettyMsgException(NettyErrorEnum.UNKNOWN, ee);
                                }
                            } else {
                                e = new NettyMsgException(NettyErrorEnum.UNKNOWN, ee);
                            }
                            recvMsg = new RecvMsg(msgId, unique, e.getCode(), e.getMessage());
                            log.error(e.getMessage(), e);
                        }
                        NettyServerMap.send(sendMsg.getClientId(), recvMsg);
                        this.log.info("服务端完成消息[{}][{}]", msgId, unique);
                    });
                } catch (Exception e) {
                    this.log.error("服务端处理消息错误", e);
                }
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        this.log.info("收到客户端ip[{}]连接", clientIp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();// 当出现异常就关闭连接
        String clientId = ctx.channel().attr(this.CLIENT_ID).get();
        NettyServerMap.removeMapValue(clientId);
    }

    // 服务端心跳检测
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                // 空闲40s之后触发 (心跳包丢失)
                Channel channel = ctx.channel();
                Attribute<Integer> counterKey = channel.attr(this.COUNTER);
                int counter = counterKey.get();
                if (counter >= 3) {
                    // 连续丢失3个心跳包 (断开连接)
                    ctx.channel().close().sync();
                    this.log.info("已与[{}]断开连接", ctx.channel().remoteAddress());
                } else {
                    counter++;
                    counterKey.set(counter);
                    this.log.info("[{}]丢失了第 [{}]个心跳包", ctx.channel().remoteAddress(), counter);
                }
            }
        }
    }

}
