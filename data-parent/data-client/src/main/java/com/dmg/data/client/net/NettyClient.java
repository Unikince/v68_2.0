package com.dmg.data.client.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.dmg.data.common.constant.MsgConst;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

/**
 * netty客户端
 */
@Component
public class NettyClient implements ApplicationRunner {
    protected static final Logger log = LoggerFactory.getLogger(NettyClientHandler.class);
    ExecutorService executor = Executors.newSingleThreadExecutor();
    /** 服务器名称 */
    @Value("${spring.application.name}")
    private String name;
    /** 主机 */
    @Value("${data.server.host}")
    private String host;
    /** 端口号 */
    @Value("${data.server.port}")
    private String portStr;
    private int port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        NettyClientMap.initClientId(name);
        this.port = Integer.parseInt(this.portStr);
        this.start();
    }

    public void start() {
        this.executor.execute(() -> {
            while (true) {
                try {
                    boolean flag = this.connect();
                    if (flag) {
                        return;
                    }
                    log.info("---------重新连接----------");
                    try {
                        Thread.sleep(1000);// 等待一秒后重连
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                } catch (Exception eeee) {
                    log.error("连接错误--------------------------------------", eeee);
                }
            }
        });
    }

    private boolean connect() {
        EventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        ChannelFuture future = null;
        try {
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new DelimiterBasedFrameDecoder(5000000, Delimiters.lineDelimiter()));
                    pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                    pipeline.addLast(new IdleStateHandler(0, 30, 0, TimeUnit.SECONDS));
                    pipeline.addLast(new NettyClientHandler(NettyClient.this));
                }
            });
            future = bootstrap.connect(this.host, this.port);
            future.sync();
            if (future.isSuccess()) {
                NettyClientMap.setChannel(future.channel());
                log.info("connect server  成功---------");
                NettyClientMap.sendMsgNotResult(MsgConst.LOGIN, null);
                return true;
            } else {
                log.info("连接失败----------准备重连---------");
                future.channel().closeFuture();
                future.channel().eventLoop().shutdownGracefully();
                return false;
            }
        } catch (Exception e) {
            try {
                future.channel().closeFuture();
                future.channel().eventLoop().shutdownGracefully();
            } catch (Exception ee) {
            }
            log.info("连接错误----------准备重连---------");
            return false;
        }
    }
}
