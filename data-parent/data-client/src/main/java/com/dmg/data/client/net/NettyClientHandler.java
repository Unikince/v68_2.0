package com.dmg.data.client.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dmg.data.common.constant.MsgConst;
import com.dmg.data.common.message.RecvMsg;
import com.dmg.data.common.util.SyncUtil;

import io.micrometer.core.instrument.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 客户端处理器
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    protected static final Logger log = LoggerFactory.getLogger(NettyClientHandler.class);
    private NettyClient nettyClient;

    public NettyClientHandler(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        log.info("客户端收到消息{}", msg);
        RecvMsg recvMsg = JSON.parseObject(msg, RecvMsg.class);
        if (recvMsg == null) {
            return;
        }
        String msgId = recvMsg.getMsgId();
        DataPushHandler pushHandle = PushHandleMgr.getHandler(msgId);
        if (pushHandle != null) {
            pushHandle.action(recvMsg.getData());
        } else {
            String unique = recvMsg.getUnique();
            if (StringUtils.isBlank(unique)) {
                return;
            }
            SyncUtil.set(unique, msg);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("------------------断线了，开始重连。。。。。。");
        this.nettyClient.start();
        ctx.close();
        ctx.channel().closeFuture();
        ctx.channel().eventLoop().shutdownGracefully();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                System.out.println("READER_IDLE");
                log.info("READER_IDLE");
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                NettyClientMap.sendMsgNotResult(MsgConst.PING, null);
                log.info("心跳发送成功!---------------------");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                log.info("ALL_IDLE");
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
