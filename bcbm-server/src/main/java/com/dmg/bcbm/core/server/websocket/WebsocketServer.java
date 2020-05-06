package com.dmg.bcbm.core.server.websocket;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dmg.bcbm.core.server.websocket.handler.NetIdleHandler;
import com.dmg.bcbm.core.server.websocket.handler.WebSocketSeverHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author zhuqd
 * @Date 2017年7月18日
 */
public class WebsocketServer implements Runnable {
	private int port;
	private String host;
	private ChannelFuture serverChannel;

	private static final Logger LOG = LoggerFactory.getLogger(WebsocketServer.class);

	public WebsocketServer() {
	}

	/**
	 * server start
	 * 
	 * @param port
	 */
	public void bind(String host, int port) {
		this.host = host;
		this.port = port;
		Thread server = new Thread(this);
		server.start();
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS)); 
							// 心跳超时处理
							ch.pipeline().addLast(new NetIdleHandler());
							ch.pipeline().addLast(new HttpServerCodec());
							ch.pipeline().addLast(new HttpObjectAggregator(8192));
							ch.pipeline().addLast(new ChunkedWriteHandler());
							// socket消息处理
							ch.pipeline().addLast(new WebSocketSeverHandler(port, host));
						}
					}).option(ChannelOption.SO_REUSEADDR, true)// socket重用
					.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)// 开启缓存池
					.childOption(ChannelOption.TCP_NODELAY, true)// 关闭TCP小包优化
					.childOption(ChannelOption.SO_KEEPALIVE, true)// 保持连接
					.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
			serverChannel = b.bind(port).sync();
			LOG.info("[LOBBY WEBSOCKET SERVER]STARTUP[OK]PORT[{}]", port);
			serverChannel.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			workGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public void close() {
		serverChannel.channel().close();
		LOG.info("[LOBBY WEBSOCKET SERVER]CLOSED[OK]PORT[{}]", port);
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

}
